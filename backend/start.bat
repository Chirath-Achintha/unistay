@echo off
echo Starting UniStay Backend...

set DB_URL=jdbc:postgresql://ep-damp-snow-aydv5h0a-pooler.c-5.us-east-2.aws.neon.tech/neondb?sslmode=require
set DB_USERNAME=neondb_owner
set DB_PASSWORD=npg_xRM9vsgNkfr0
set ADMIN_EMAIL=admin@gmail.com
set ADMIN_PASSWORD=admin123
set CLOUDINARY_CLOUD_NAME=pzw11iut
set CLOUDINARY_API_KEY=435812181276724
set CLOUDINARY_API_SECRET=dOpqVo32n08XANg5b5qUk08aTJg

call mvnw.cmd spring-boot:run
