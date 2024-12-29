docker run ^
    -p 9005:9005 ^
    -v D:\fifth_storage:/home/fifth_storage/ ^
    --name fifth_container ^
    fifth_gradle ^
    --network host