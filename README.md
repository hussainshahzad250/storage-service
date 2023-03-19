# storage-service

## Upload File

```
curl --location 'localhost:8080/cloud/api/uploadFile' \
--header 'Cookie: JSESSIONID=2FCC604489B81CA6128E0403B65BEB66' \
--form 'file=@"/C:/Users/Ajay/Pictures/Site-Visit-Report.jpg"'
```


## Download File
```
curl --location 'localhost:8080/cloud/api/download/1675501331272_th.jpg' \
--header 'Cookie: JSESSIONID=2FCC604489B81CA6128E0403B65BEB66'
```
