package xtensus.ldap.utils;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1416132138315457558L;

	// extra instance variables
	final int id;
	final String fullname;
	final String email;
	final String title;
	final String username;
	final String password;
	final boolean enabled;
	final boolean accountNonExpired;
	final boolean credentialsNonExpired;
	final boolean accountNonLocked;
	final Collection<GrantedAuthority> authorities;

	public CustomUserDetails(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Collection<GrantedAuthority> authorities,
			String fullname, String email, String title, int id) {
		super();
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities = authorities;
		this.id = id;
		this.fullname = fullname;
		this.email = email;
		this.title = title;
	}

	public int getId() {
		return this.id;
	}

	public String getFullname() {
		return this.fullname;
	}

	public String getEmail() {
		return this.email;
	}

	public String getTitle() {
		return this.title;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}
