# elasticsearch-glacier

Push your ES shards into AWS s3 and glacier

## Usage

Make sure you have the following in `config.clj` on the classpath:

```clojure
{:maclaren/aws-key "................."
 :maclaren/aws-secret-key "........................................"
 :maclaren/aws-bucket "bukkit"}
```
