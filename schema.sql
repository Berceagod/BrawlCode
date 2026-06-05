CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY,
    username TEXT NOT NULL,
    type TEXT NOT NULL,
    elo_rating INTEGER,
    premium_badge TEXT,
    access_level INTEGER
);

CREATE TABLE IF NOT EXISTS problems (
    id INTEGER PRIMARY KEY,
    title TEXT NOT NULL,
    difficulty TEXT NOT NULL,
    type TEXT NOT NULL,
    time_limit REAL,
    schema_name TEXT
);

CREATE TABLE IF NOT EXISTS matches (
    id INTEGER PRIMARY KEY,
    player1_id INTEGER,
    player2_id INTEGER,
    problem_id INTEGER,
    is_finished INTEGER,
    FOREIGN KEY(player1_id) REFERENCES users(id),
    FOREIGN KEY(player2_id) REFERENCES users(id),
    FOREIGN KEY(problem_id) REFERENCES problems(id)
);

CREATE TABLE IF NOT EXISTS submissions (
    id INTEGER PRIMARY KEY,
    user_id INTEGER,
    code_snippet TEXT,
    is_correct INTEGER,
    FOREIGN KEY(user_id) REFERENCES users(id)
);