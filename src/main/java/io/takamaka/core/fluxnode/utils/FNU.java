/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.takamaka.core.fluxnode.utils;

import io.takamaka.wallet.utils.FileHelper;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public static final Path getFluxNodeBase() {
        Path res = Paths.get(FileHelper.getDefaultApplicationDirectoryPath().toString(), FLUX_NODE_BASE_FOLDER);
        return res;
    }

    public static final Path getFluxConfigsPath() {
        Path res = Paths.get(getFluxNodeBase().toString(), FLUX_NODE_CONFIGS);
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
    }

}
