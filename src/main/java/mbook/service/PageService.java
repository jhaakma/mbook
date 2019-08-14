package mbook.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import mbook.model.Page;
import mbook.model.Role;

@Service
public class PageService {
    
    private static ArrayList<Page> pages = new ArrayList<Page>();
    static {
        pages.add(
            Page.builder()
            .title("Home")
            .href("/")
            .showInMenu(false)
            .build()
        );
        pages.add(
            Page.builder()
            .title("Videos")
            .href("/videos")
            .requiredRole(Role.Type.ADMIN.getValue())
            .requiredRole(Role.Type.USER.getValue())
            .build()
        );
        pages.add(
                Page.builder()
                .title("User List")
                .href("/userlist")
                .requiredRole(Role.Type.ADMIN.getValue())
                .build()
            );
        pages.add(
            Page.builder()
            .title("Dashboard")
            .href("/dashboard")
            .requiredRole(Role.Type.ADMIN.getValue())
            .build()
        );
    }
    

    public ArrayList<Page> getAuthorisedPages( List<GrantedAuthority> userAuthorities ) {
        ArrayList<Page> authorisedPages = new ArrayList<Page>();
        for ( Page page : pages ) {
            if ( page.getShowInMenu() && isAuthorised(page, userAuthorities )) {
                authorisedPages.add(page);
            }
        }
        
        return authorisedPages;
    }
    
    private static Boolean isAuthorised(Page page, List<GrantedAuthority> userAuthorities ) {
        //No required roles, anyone can access
        if ( page.getRequiredRoles().size() == 0 ) {
            return true;
        }
        
        for ( GrantedAuthority role : userAuthorities ) {
            for ( String requiredRole : page.getRequiredRoles() ) {
                if ( role.getAuthority().equals(requiredRole) ) {
                    return true;
                }
            }
        }
        return false;
    }
    
}
