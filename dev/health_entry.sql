/*
-- Photo attachments
CREATE TABLE health_entry_photos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    health_entry_id BIGINT NOT NULL,
    photo_url VARCHAR(500) NOT NULL,
    caption VARCHAR(255),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (health_entry_id) REFERENCES health_entries(id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX idx_health_entries_pet_date ON health_entries(pet_id, entry_date);
CREATE INDEX idx_health_entries_user_date ON health_entries(user_id, entry_date);
CREATE INDEX idx_activities_entry ON health_entry_activities(health_entry_id);
CREATE INDEX idx_mood_entry ON health_entry_mood(health_entry_id);
CREATE INDEX idx_bathroom_entry ON health_entry_bathroom(health_entry_id);

*/

create or replace table pet_diary.health_entry (
    id              bigint          primary key auto_increment comment 'health entry id',
    pet_id          bigint          not null,
    user_id         bigint          not null,
    entry_date      date            not null comment 'one entry per pet, per day',
    weight          decimal(5,2),
    weight_unit     enum('kg', 'lbs') default 'kg',
    energy_level_id int             comment 'references energy_level table (but no FK constraint)',
    notes           text            comment 'user free-form diary entry',
    created_at      datetime        default current_timestamp(),
    modified_at     datetime        default current_timestamp() on update current_timestamp(),        

    constraint FK_health_entry_pet foreign key (pet_id) references pet_diary.pet (pet_id) on delete cascade,
    constraint FK_health_entry_user foreign key (user_id) references pet_diary.usr (user_id) on delete cascade,
    constraint unique_pet_date unique (pet_id, entry_date)
)
    comment 'health entry master table';

create or replace table pet_diary.energy_level (
    id              int             primary key auto_increment,
    value           varchar(20)     not null unique comment 'no spaces, use underscore eg) very_low',
    display         varchar(25)     not null comment 'what is actually shown to users',
    sort_order      int             not null unique, 
    is_active       boolean         default true
)
    comment 'health entry energy level values; do not delete values - set is_active to false';

insert into pet_diary.energy_level (value, display, sort_order) values
("very_low", "Very low", 1),
("low", "Low", 2),
("normal", "Normal", 3),
("high", "High", 4),
("very_high", "Very high", 5);

create or replace table pet_diary.activity_type (
    id              int             primary key auto_increment,
    value           varchar(25)     not null unique comment 'no spaces, use underscore eg) indoor_play',   
    display         varchar(25)     not null comment 'what is actually shown to users',
    sort_order      int             not null unique, 
    is_active       boolean         default true
)
    comment 'health entry activity types; do not delete values - set is_active to false';

insert into pet_diary.activity_type (value, display, sort_order) values 
("walking", "Walking", 1),
("running", "Running", 2),
("indoor_playing", "Indoor playing", 3),
("swimming", "Swimming", 4);

create or replace table pet_diary.health_entry_activity (
    id              bigint          primary key auto_increment,
    health_entry_id bigint          not null,
    activity_type_id    int         not null,
    duration        int             comment 'minutes',
    start_at        time            comment 'activity start time (HH:MM)',
    notes           varchar(255),    

    constraint FK_activity_health_entry foreign key (health_entry_id) references pet_diary.health_entry (id) on delete cascade
)
    comment 'health entry activity master table';
