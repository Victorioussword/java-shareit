package ru.practicum.shareit.user.repository;

//@Slf4j
//@Repository
//@RequiredArgsConstructor
public class InMemoryUserRepository {  // implements UserRepository {
 //   private long id = 1;
//    private Map<Long, User> users = new HashMap<>();
//    private Set<String> usersByEmail = new HashSet<>();


 //   public UserDto createUser(UserDto userDto) {
//        userDto.setId(id);
 //       id++;
 //       users.put(userDto.getId(), UserMapper.toUser(userDto));
 //       usersByEmail.add(userDto.getEmail());
//        log.info("InMemoryUserRepository - createUser(). ДОбавлен {}", userDto.toString());
//        return userDto;
//    }

//    public User getById(long id) {
 //       if (users.containsKey(id)) {
 //           User userForReturn = users.get(id);
  //          log.info("InMemoryUserRepository - метод getById(). Возвращен {} ", userForReturn.toString());
 //           return userForReturn;
//        } else {
 //           log.info("InMemoryUserRepository - метод getById(). User {} не найден.", id);
 //           throw new NotFoundException("User не найден");
//        }
//    }

 //   public User update(User user, String oldEmail) {
 //       usersByEmail.remove(oldEmail);
 //       usersByEmail.add(user.getEmail());
 //       log.info("InMemoryUserRepository - update(). Обновлен {}", user.toString());
 //       return user;
 //   }

 //   public void delById(long id) {
 //       usersByEmail.remove(users.get(id).getEmail());
 //       log.info("InMemoryUserRepository - delById(). Удален пользователь с id {}", id);
 //       users.remove(id);
 //       return;
 //   }

  //  public Set<String> getUsersByEmail() {
 //       return Set.copyOf(usersByEmail);
 //   }

 //   public Map<Long, User> getUsers() {
  //      return Map.copyOf(users);
 //   }

  //  public List<User> getAll() {
  //      List<User> usersRForReturn = users.values().stream().collect(Collectors.toList());
 //       return usersRForReturn;
 //   }
}