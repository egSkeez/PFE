package xtensus.ldap.utils;

import java.util.Collection;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

public class CustomUserDetailsContextMapper implements UserDetailsContextMapper {

	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx,
			String username, Collection<GrantedAuthority> authorities) {
		int id = -1;
		String fullname = "";
		String email = "";
		String title = "";
		Attributes attributes = ctx.getAttributes();
		try {
			id = Integer.parseInt((String) attributes.get("uid").get());
			fullname = (String) attributes.get("displayName").get();
			email = (String) attributes.get("mail").get();
			try {
				title = (String) attributes.get("street").get();
			} catch (NullPointerException e) {

			}
		} catch (NamingException e) {
			e.printStackTrace();
		}

		CustomUserDetails details = new CustomUserDetails(username, "", true,
				true, true, true, authorities, fullname, email, title, id);
		return details;
	}

	@SuppressWarnings("static-access")
	@Override
	public void mapUserToContext(UserDetails arg0, DirContextAdapter arg1) {
		try {
			arg1.addToEnvironment(arg1.SECURITY_PRINCIPAL, arg0);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

}
