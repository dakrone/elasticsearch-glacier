# elasticsearch-glacier

Push your ES shards into AWS s3 and glacier

## Usage

Make sure you have the following in `config.clj` on the classpath:

```clojure
{:maclaren/aws-key "................."
 :maclaren/aws-secret-key "........................................"
 :maclaren/aws-bucket "bukkit"}
```

## Note

This project was written in under 24 hours, and as such is not up to
production-quality. Treat with care!
