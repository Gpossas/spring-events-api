CREATE TABLE coupons (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    discount INTEGER NOT NULL,
    expiration_date TIMESTAMP,
    event_id UUID,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);