package burundi.ilucky.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "_user", indexes = {
        @Index(name = "idx_total_star", columnList = "totalStar")})
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date addTime;
    
    private long totalPlay;

    private long totalVnd;
    @Column(nullable = false)
    private long totalStar;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
         return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
