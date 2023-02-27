package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@Setter

public class UserDto {

    private long id;
    @NotBlank(groups = Create.class)
    private String name;
    @NotEmpty(groups = Create.class)
    @Email(groups = {Create.class, Update.class})
    private String email;
}


//    Так же и здесь, можно было бы использовать валидацию по группам)
//        Для имени:
//@NotBlank(groups = {Create.class})

//Для почты аннотацию @Email следует учитываться для обновления и создания,
// так как, если она не будет передана вообще, то все будет окей,
// а если будет, тогда она будет проверена на соответствие почте, а вот аннотацию
//@NotEmpty только для создания) Так как при обновлении передавать ее не обязательно)
