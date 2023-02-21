/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.takamaka.core.fluxnode.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.takamaka.core.fluxnode.domain.UserPasswordBean;
import io.takamaka.wallet.utils.FileHelper;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Giovanni Antino giovanni.antino@takamaka.io
 */
@Slf4j
public class FNU {

    public static final String WEB_INTERFACE_USERS_PASSWORDS = "web_interface_users_passwords.json";
    public static final String FLUX_NODE_BASE_FOLDER = "flux_node";
    public static final String FLUX_NODE_CONFIGS = "configs";

    public static enum WEB_ROLES {
        ADMIN,
        MONITOR,
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

    public static final void initFluxFiles() throws IOException {
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
     * Password encoding example null null null null null     {@code 
     *  PasswordEncoder encoder =
     *      PasswordEncoderFactories.createDelegatingPasswordEncoder();
     *  log.info(encoder.encode("monitor_password"));
     * }
     */
    public static final void initStubSecurityPrincipals() throws JsonProcessingException, IOException {
        final Set<UserPasswordBean> principals = new ConcurrentSkipListSet<>();
        principals.add(new UserPasswordBean(
                "admin",
                //all roles
                Arrays.asList(WEB_ROLES.values()).stream().map((wr) -> wr.name()).toList(),
                //admin_password
                "{bcrypt}$2a$10$csgn3rKfs5nbxnp/fEC93..0ZBk8ud7NdHFeziW/N4dhc.vlsDj7y"));
        principals.add(new UserPasswordBean(
                "user",
                Arrays.asList(new WEB_ROLES[]{WEB_ROLES.USER}).stream().map((wr) -> wr.name()).toList(),
                //user_password
                "{bcrypt}$2a$10$n8r/ZKeL984X7cnI9ottVePGNKKZcWmwbvEVydOZcF4WerQJrf7zW"));
        principals.add(new UserPasswordBean(
                "monitor",
                Arrays.asList(new WEB_ROLES[]{WEB_ROLES.MONITOR}).stream().map((wr) -> wr.name()).toList(),
                //monitor_password
                "{bcrypt}$2a$10$TeLv0CM6LAYvYD0aMji3buLIHL9SJ4efqYSYwrqwKNvanoqkRPbrC"));
        String jsonPrincipals = FNUSerializer.userPasswordBeanListToJson(principals);
        writeStringToFileUTF8IfNotExists(getFluxConfigsPath(), WEB_INTERFACE_USERS_PASSWORDS, jsonPrincipals);

    }

}
