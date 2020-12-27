/**
 * Created by mary.mikhaleva on 01.05.20.
 */

package com.spbstu.weblabresearch.dbo;

import com.spbstu.weblabresearch.util.StringTypeVerifier;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Data
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String login;

    String surname;

    String name;

    String patronymic;

    @Enumerated(EnumType.STRING)
    Sex sex;

    int passportSeries;

    int passportNumber;

    @Enumerated(EnumType.STRING)
    Role role;

    String phone;

    String password;

    public void validate() throws Exception {
        StringTypeVerifier.notEmptyString(login, "Введите логин");
        StringTypeVerifier.notEmptyString(surname, "Фамилия должна быть указана");
        StringTypeVerifier.notEmptyString(name, "Имя должно быть указано");
        StringTypeVerifier.notEmptyString(patronymic, "Отчество должно быть указано");
        StringTypeVerifier.notEmptyString(phone, "Введите телефон");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var res = new ArrayList<Role>();
        res.add(getRole());
        return res;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
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
