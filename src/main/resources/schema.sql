create  TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL unique,
  CONSTRAINT pk_user PRIMARY KEY (id)
);

create table if not exists items (
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar (50) not null,
description varchar (200) not null,
available  boolean not null,
owner_id BIGINT not null,
FOREIGN KEY (owner_id) REFERENCES users (id)
);


create TABLE IF NOT EXISTS BOOKINGS (
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
startTime TIMESTAMP WITHOUT TIME ZONE not null,
endTime TIMESTAMP WITHOUT TIME ZONE not null,
itemId BIGINT not null ,
booker BIGINT not null ,
status varchar (16) not null,
FOREIGN KEY (booker) REFERENCES users (id),
FOREIGN KEY (itemId) REFERENCES items (id)
);

 create TABLE IF NOT EXISTS COMMENTS (
   id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   text varchar (4000) not null,
   author BIGINT not null  ,
   itemId BIGINT not null ,
   created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   FOREIGN KEY (author) REFERENCES users (id),
   FOREIGN KEY (itemId) REFERENCES items (id)
   );



