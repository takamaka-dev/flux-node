/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.takamaka.core.fluxnode.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.takamaka.core.fluxnode.domain.UserPasswordBean;
import io.takamaka.core.fluxnode.exceptions.FNUException;
import io.takamaka.core.fluxnode.exceptions.InsufficientRolePrivileges;
import io.takamaka.core.fluxnode.exceptions.MissingRoleInformationsException;
import io.takamaka.core.fluxnode.exceptions.MissingRolePrivilegeException;
import io.takamaka.core.fluxnode.exceptions.PasswordMapFormatException;
import io.takamaka.core.fluxnode.exceptions.PasswordMapSecurityException;
import io.takamaka.wallet.utils.FileHelper;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Giovanni Antino giovanni.antino@takamaka.io
 */
@Slf4j
public class FNU {

    public static final String WEB_INTERFACE_USERS_PASSWORDS = "web_interface_users_passwords.json";
    public static final String FLUX_NODE_BASE_FOLDER = "flux_node";
    public static final String FLUX_NODE_CONFIGS = "configs";
    public static final Object PRINCIPAL_UPDATE_LOCK = new Object();

    public static enum WEB_ROLES {
        ADMIN,
        MONITOR,
        /**
         * create, remove or update users
         */
        USER_MANAGE,
        USER
    };

    public static final Path getFluxNodeBase() {
        final Path res = Paths.get(FileHelper.getDefaultApplicationDirectoryPath().toString(), FLUX_NODE_BASE_FOLDER);
        return res;
    }

    public static final Path getFluxConfigsPath() {
        final Path res = Paths.get(getFluxNodeBase().toString(), FLUX_NODE_CONFIGS);
        return res;
    }

    public static final void createDirIfNotExists(Path p) throws IOException {
        log.info("dir " + p.toString());
        if (!FileHelper.directoryExists(p)) {
            log.info("missing " + p.toString());
            FileHelper.createDir(p);
        }
        log.info("initialized " + p.toString());
    }

    public static final void initFluxFiles() throws IOException, JsonProcessingException, FNUException {
        createDirIfNotExists(getFluxNodeBase());
        createDirIfNotExists(getFluxConfigsPath());
        initStubSecurityPrincipals();
    }

    public static final void writeStringToFileUTF8IfNotExists(Path p, String filename, String content) throws IOException {
        Path filePath = Paths.get(p.toString(), filename);
        log.info("write file " + filePath.toString());
        if (!FileHelper.fileExists(filePath)) {
            log.info("file does not exists ");
            FileHelper.writeStringToFileUTF8(p, filename, content, false);
            log.info("file written");
        } else {
            log.info("file exists, doing nothing");
        }
    }

    /**
     * Password encoding example {@code
     *  PasswordEncoder encoder =
     *      PasswordEncoderFactories.createDelegatingPasswordEncoder();
     *  log.info(encoder.encode("monitor_password"));
     * }
     */
    public static final void initStubSecurityPrincipals() throws JsonProcessingException, IOException, FNUException {
        final Map<String, UserPasswordBean> principals = new ConcurrentSkipListMap<>();
        principals.put("admin", new UserPasswordBean(
                "admin",
                //all roles
                Arrays.asList(WEB_ROLES.values()),
                //admin_password
                "{bcrypt}$2a$10$csgn3rKfs5nbxnp/fEC93..0ZBk8ud7NdHFeziW/N4dhc.vlsDj7y"));
        principals.put("user", new UserPasswordBean(
                "user",
                Arrays.asList(new WEB_ROLES[]{WEB_ROLES.USER}),
                //user_password
                "{bcrypt}$2a$10$n8r/ZKeL984X7cnI9ottVePGNKKZcWmwbvEVydOZcF4WerQJrf7zW"));
        principals.put("monitor", new UserPasswordBean(
                "monitor",
                Arrays.asList(new WEB_ROLES[]{WEB_ROLES.MONITOR}),
                //monitor_password
                "{bcrypt}$2a$10$TeLv0CM6LAYvYD0aMji3buLIHL9SJ4efqYSYwrqwKNvanoqkRPbrC"));
        String jsonPrincipals = FNUSerializer.userPasswordBeanMapToJson(principals);
        checkPrincipalsFileFormat(principals, true);
        writeStringToFileUTF8IfNotExists(getFluxConfigsPath(), WEB_INTERFACE_USERS_PASSWORDS, jsonPrincipals);
    }

    private static final Map<String, UserPasswordBean> readPrincipalsFile() throws IOException {
        final String userPassSetJson = FileHelper.readStringFromFileUTF8(Paths.get(getFluxConfigsPath().toString(), WEB_INTERFACE_USERS_PASSWORDS));
        return FNUSerializer.getUserPasswordBeanMap(userPassSetJson);
    }

    private static final void checkPrincipalsFileFormat(Map<String, UserPasswordBean> principalsMap, boolean isDefaultEncoded) throws FNUException {
        //map name -> principals
        final String[] principals = principalsMap.keySet().toArray(String[]::new);
        final ConcurrentSkipListMap<String, FNUException> exMapper = new ConcurrentSkipListMap<>();
        final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        if (!principalsMap.containsKey("admin")) {
            throw new PasswordMapFormatException("missing mandatory user admin");
        }
        UserPasswordBean adminUPB = principalsMap.get("admin");
        WEB_ROLES[] adminRoles = adminUPB.getRoles().toArray(WEB_ROLES[]::new);
        Arrays.sort(adminRoles);
        WEB_ROLES[] referenceRoles = WEB_ROLES.values();
        Arrays.sort(referenceRoles);
        if (Arrays.compare(adminRoles, referenceRoles) != 0) {
            throw new PasswordMapFormatException("admin roles does not match refence roles");
        }
        Arrays.stream(principals).forEach(p -> {
            UserPasswordBean uP = principalsMap.get(p);
            if (!uP.getUsername().equals(p)) {
                exMapper.put(p, new PasswordMapFormatException("invalid key <-> name association for " + p + " must be equal"));
            }

            if (!isDefaultEncoded) {
                log.info("password not default encoded, additional security check performed");
                if (!passwordEncoder.upgradeEncoding(uP.getPassword())) {
                    exMapper.putIfAbsent(p, new PasswordMapSecurityException("encoded password should be encoded again for better security"));
                }
            }

        });
        if (!exMapper.isEmpty()) {
            throw exMapper.firstEntry().getValue();
        }

    }

    public static final boolean checkRolePrivilege(Set<WEB_ROLES> callerExecutionPrivilege, WEB_ROLES roleRquired) {
        //admin override
        if (callerExecutionPrivilege.contains(WEB_ROLES.ADMIN)) {
            return true;
        }
        //role check
        return callerExecutionPrivilege.contains(roleRquired);
    }

    public static final boolean canAssignRoles(Set<WEB_ROLES> callerExecutionPrivilege, WEB_ROLES[] rolesToBeAssigned) {
        //admin override
        if (callerExecutionPrivilege.contains(WEB_ROLES.ADMIN)) {
            return true;
        }
        final ConcurrentSkipListSet<Boolean> missingRole = new ConcurrentSkipListSet<>();
        Arrays.stream(rolesToBeAssigned).forEach(r -> {
            if (!callerExecutionPrivilege.contains(r)) {
                missingRole.add(Boolean.TRUE);
            }
        });
        return !missingRole.contains(Boolean.TRUE);
    }

    public static final void updatePrincipal(String principal, WEB_ROLES[] roles, String plainOldPassword, String plainNewPassword, Set<WEB_ROLES> callerExecutionPrivilege) throws IOException, FNUException {
        synchronized (PRINCIPAL_UPDATE_LOCK) {
            //sync
            final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            //passwordEncoder.matches(plainOldPassword, plainNewPassword)
            //read the bean
            final Map<String, UserPasswordBean> principalsMap = readPrincipalsFile();
            Arrays.sort(roles);
            //check input file
            checkPrincipalsFileFormat(principalsMap, true);
            UserPasswordBean upb;
            if ("admin".equals(principal) && !checkRolePrivilege(callerExecutionPrivilege, WEB_ROLES.ADMIN)) {
                throw new InsufficientRolePrivileges("only ADMIN role can update admin user");
            }
            if (Arrays.binarySearch(roles, WEB_ROLES.ADMIN) >= 0 && !checkRolePrivilege(callerExecutionPrivilege, WEB_ROLES.ADMIN)) {
                throw new InsufficientRolePrivileges("only ADMIN role can manage ADMIN privilege");
            }
            WEB_ROLES[] rolesToBeChecked;
            if (roles == null || roles.length <= 0) {
                rolesToBeChecked = null;
            } else {
                if (canAssignRoles(callerExecutionPrivilege, roles)) {
                    rolesToBeChecked = roles;
                } else {
                    throw new MissingRolePrivilegeException("The current user cannot assign an uncontrolled role.");
                }
            }

            if (principalsMap.containsKey(principal)) {
                //user exists
                upb = principalsMap.get(principal);
                if ( //emulate root passwd no password required for admin
                        !checkRolePrivilege(callerExecutionPrivilege, WEB_ROLES.ADMIN)
                        //user manager can alter other non admin roles password
                        || !checkRolePrivilege(callerExecutionPrivilege, WEB_ROLES.USER_MANAGE)
                        //if I know the old password I can update the password
                        || !passwordEncoder.matches(plainOldPassword, upb.getPassword())) {
                    throw new PasswordMapSecurityException("password mismatch or not authorized");
                }
            } else {
                //user does not exists
                upb = new UserPasswordBean(principal, null, passwordEncoder.encode(plainNewPassword));
                if (rolesToBeChecked == null) {
                    throw new MissingRoleInformationsException("roles undefined for new user");
                }
            }
            principalsMap.put(upb.getUsername(), upb);
            checkPrincipalsFileFormat(principalsMap, true);
            //move backup + write
            FileHelper.rename(Paths.get(getFluxConfigsPath().toString(), WEB_INTERFACE_USERS_PASSWORDS), Paths.get(getFluxConfigsPath().toString(), WEB_INTERFACE_USERS_PASSWORDS + (new Date()).getTime()), Boolean.TRUE);
            String jsonPrincipals = FNUSerializer.userPasswordBeanMapToJson(principalsMap);
            writeStringToFileUTF8IfNotExists(getFluxConfigsPath(), WEB_INTERFACE_USERS_PASSWORDS, jsonPrincipals);
            //sync -- end
        }
    }

}
