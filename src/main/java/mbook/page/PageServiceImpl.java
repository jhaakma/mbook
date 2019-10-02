package mbook.page;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class PageServiceImpl implements PageService {
    
    @Autowired
    private PageRepository pageRepository;
    
    public ArrayList<Page> getPages() {
        return (ArrayList<Page>) pageRepository.findAll();
    }

    public ArrayList<Page> getAuthorisedPages( List<GrantedAuthority> userAuthorities ) {
        ArrayList<Page> authorisedPages = new ArrayList<Page>();
        for ( Page page : getPages() ) {
            if ( isAuthorised(page, userAuthorities )) {
                authorisedPages.add(page);
            }
        }
        
        return authorisedPages;
    }
    
    private static Boolean isAuthorised(Page page, List<GrantedAuthority> userAuthorities ) {
        //No required roles, anyone can access
        if ( page.getAllowedRoles().size() == 0 ) {
            return true;
        }
        
        for ( GrantedAuthority role : userAuthorities ) {
            for ( String requiredRole : page.getAllowedRoles() ) {
                if ( role.getAuthority().equals(requiredRole) ) {
                    return true;
                }
            }
        }
        return false;
    }
}
