/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.takamaka.core.fluxnode.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.takamaka.core.fluxnode.domain.UserPasswordBean;
import io.takamaka.wallet.utils.TkmTextUtils;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Giovanni Antino giovanni.antino@takamaka.io
 */
public class FNUSerializer {

    // UserPasswordBean
    public static final TypeReference<Set<UserPasswordBean>> type_setUserPasswordBean = new TypeReference<>() {
    };

    public static final String userPasswordBeanToJson(UserPasswordBean upb) throws JsonProcessingException {
        return TkmTextUtils.getJacksonMapper().writeValueAsString(upb);
    }

    public static final UserPasswordBean getUserPasswordBean(String upbJson) throws JsonProcessingException {
        return TkmTextUtils.getJacksonMapper().readValue(upbJson, UserPasswordBean.class);
    }

    public static final String userPasswordBeanListToJson(Set<UserPasswordBean> upbList) throws JsonProcessingException {
        return TkmTextUtils.getJacksonMapper().writerWithDefaultPrettyPrinter().writeValueAsString(upbList);
    }

    public static final Set<UserPasswordBean> getUserPasswordBeanList(String upbJsonList) throws JsonProcessingException {
        return TkmTextUtils.getJacksonMapper().readValue(upbJsonList, type_setUserPasswordBean);
    }

}
