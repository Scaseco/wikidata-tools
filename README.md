# wikidata-tools
Fast and simple tooling for Wikidata N-Triples.

## Setup

Groovy is required.

```bash
chmod +x wikidata-release-status.groovy
```

## Fetch release status in JSON

```bash
./wikidata-release-status.groovy
```

```json
{
  "truthy-BETA": [
    {
      "date": 20250625,
      "url": "https://dumps.wikimedia.org/wikidatawiki/entities/20250625/wikidata-20250625-truthy-BETA.nt.bz2"
    }
  ],
  "lexemes-BETA": [
    {
      "date": 20250627,
      "url": "https://dumps.wikimedia.org/wikidatawiki/entities/20250627/wikidata-20250627-lexemes-BETA.nt.bz2"
    }
  ]
}
```

Use `jq` for post processing, such as:

```bash
/wikidata-release-status.groovy | jq -r '."truthy-BETA"[0].url'

https://dumps.wikimedia.org/wikidatawiki/entities/20250625/wikidata-20250625-truthy-BETA.nt.bz2
```

