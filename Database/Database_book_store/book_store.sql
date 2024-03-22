
create schema book_store;
use book_store;

CREATE TABLE members (
  fname VARCHAR(20) not null,
  lname VARCHAR(20) not null,
  adress VARCHAR(50) not null,
  city VARCHAR(30) not null,
  state VARCHAR(20) not null,
  zip CHAR(5) not null,
  phone VARCHAR(12) null,
  email VARCHAR(40) null,
  userid INT not null AUTO_INCREMENT,
  password VARCHAR(64) null,
  creditcardtype VARCHAR(10) null,
  creditcardnumber CHAR(16) null,
  primary key (userid)
);

CREATE TABLE books (
  isbn CHAR(10) not null,
  author VARCHAR(100) not null,
  title VARCHAR(128) not null,
  price FLOAT not null,
  subject VARCHAR(30) not null,
  primary key (isbn)
);

CREATE TABLE orders (
  userid INT not null,
  ono INT not null auto_increment,
  recived DATE not null,
  shipped DATE null,
  shipAddress VARCHAR(50) null,
  shipCity VARCHAR(30) null,
  shipState VARCHAR(20) null,
  shipZip CHAR(5) null,
  primary key (ono),
  foreign key (userid) references members(userid)
);

CREATE TABLE odetails (
  ono INT null,
  isbn CHAR(10) null,
  qty INT not null,
  price FLOAT not null,
  foreign key (ono) references orders(ono),
  foreign key (isbn) references books(isbn)
);

CREATE TABLE cart (
  userid INT null,
  isbn CHAR(10) null,
  qty INT not null,
  foreign key (userid) references members(userid),
  foreign key (isbn) references books(isbn)
);
