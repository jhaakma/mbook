package mbook.page;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public interface PageService {
    public ArrayList<Page> getPages();
    public ArrayList<Page> getAuthorisedPages( List<GrantedAuthority> userAuthorities );
  
}
