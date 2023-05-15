# curl -X POST -H "Content-Type: application/json" localhost:8080/books -d \
#   '{"name":"Words of Radiance", "isbn":"9781250166531", "genreId":"1"}'
curl -X PUT -H "Content-Type: application/json" localhost:8080/books -d \
  '{"id": "3", "name":"Words of Radiance", "isbn":"9781250166531", "genreId":"1"}'