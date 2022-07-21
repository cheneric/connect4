## `connect4`

### Overview

I wrote this Java implementation of Connect 4 in about 8 hours.

Although the default board size matches the original game (7 columns x 6 rows), the 
actual board size and number of tokens in a row required to win is actually configurable 
in the `GravityBoard` constructor.

### How to run

There are 2 ways to run the game:

1. Natively
2. In a Docker container

#### 1. Run natively

To run in a native environment, **ensure OpenJDK 18.0.1.1+ is installed**.

Then, in the project root directory, run the following command:

```shell
connect4  $  ./gradlew run --console=plain
```

#### 2. Run in a Docker container

To run in a Docker container, **ensure you have Docker installed**.

Then, in the project root directory, run the following commands:

1. Build the Docker image:

```shell
connect4  $ docker build --rm -t echen/connect4:latest .
```

2. Run the Docker image in interactive mode:

```shell
connect4  $ docker run -it --name connect4 echen/connect4:latest
```

### Gameplay

Black starts first and then alternates turns with Red.

The first player to connect 4 of their tokens in a row wins.

Note that the column indexes for selecting which column a player wishes to play in are 0-based 
(i.e. 0-6, not 1-7).  