package com.vincendp.RedditClone.Utility;

import com.vincendp.RedditClone.Model.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextUtility {

    public CustomUserDetails getUserDetailsFromSecurityContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = null;
        CustomUserDetails userDetails = null;

        if(authentication != null) {
            principal = authentication.getPrincipal();
        }
        if(principal instanceof CustomUserDetails){
            userDetails = (CustomUserDetails) principal;
        }

        return userDetails;
    }
}
