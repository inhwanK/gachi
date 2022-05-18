package org.deco.gachicoding.config;

import org.deco.gachicoding.dto.user.UserResponseDto;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession httpSession = request.getSession();
        UserResponseDto userResponseDto = (UserResponseDto) httpSession.getAttribute("user");
//        response.

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                    @Nullable ModelAndView modelAndView) throws Exception {


    }
}
