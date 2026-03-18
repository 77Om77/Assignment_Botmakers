package com.authrbac.mapper;

import com.authrbac.dto.AuthResponse;
import com.authrbac.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// MapStruct generates the implementation of this interface at compile time.
// It looks at field names and types and creates the mapping code automatically.
// componentModel = "spring" means MapStruct makes this a Spring bean (@Component)
// so we can inject it with @Autowired or @RequiredArgsConstructor.
@Mapper(componentModel = "spring")
public interface UserMapper {

    // User.role is an ERole enum, AuthResponse.role is a String.
    // MapStruct handles this automatically with .toString() since it's an enum.
    // But we name it explicitly to be clear.
    @Mapping(target = "role", expression = "java(user.getRole().name())")
    // token is not part of User, so we tell MapStruct to ignore it here.
    // We'll set the token manually in AuthService after generating it.
    @Mapping(target = "token", ignore = true)
    AuthResponse toAuthResponse(User user);
}
