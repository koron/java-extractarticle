# Extract Article from HTML

java-extractarticle extracts an article (main contents) from HTML.

* [Pre-requirements](#pre-requirements)
* [Getting started](#getting-started)
* [How to release](#how-to-release)

## Pre-requirements

java-extractarticle is available on [GitHub Packages][gp].
([Japanese version][gp-ja])

[gp]:https://docs.github.com/en/packages
[gp-ja]:https://docs.github.com/ja/packages

### for Maven

1.  Create a personal access token with `read:packages` permission at <https://github.com/settings/tokens>

2.  Put username and token to your ~/.m2/settings.xml file with `<server>` tag.

    ```pom
    <settings>
      <servers>
        <server>
          <id>github</id>
          <username>USERNAME</username>
          <password>YOUR_PERSONAL_ACCESS_TOKEN_WITH_READ</password>
        </server>
      </servers>
    </settings>
    ```

3.  Add a repository to your `repositories` section in project's pom.xml file.

    ```pom
    <repository>
      <id>github</id>
      <url>https://maven.pkg.github.com/koron/java-extractarticle</url>
    </repository>
    ```

4.  Add a `<dependency>` tag to your `<dependencies>` tag.

    ```pom
    <dependency>
      <groupId>net.kaoriya</groupId>
      <artifactId>extractarticle</artifactId>
      <version>0.0.1</version>
    </dependency>
    ```

Please read [public document](https://docs.github.com/en/packages/guides/configuring-apache-maven-for-use-with-github-packages) also. ([Japanese](https://docs.github.com/ja/packages/guides/configuring-apache-maven-for-use-with-github-packages))

### for Gradle

1.  Create a personal access token with `read:packages` permission at <https://github.com/settings/tokens>

2.  Put username and token to your ~/.gradle/gradle.properties file.

    ```
    gpr.user=YOUR_USERNAME
    gpr.key=YOUR_PERSONAL_ACCESS_TOKEN_WITH_READ:PACKAGES
    ```

3.  Add a repository to your `repositories` section in build.gradle file.

    ```groovy
    maven {
        url = uri("https://maven.pkg.github.com/koron/java-extractarticle")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") ?: System.getenv("TOKEN")
        }
    }
    ```

4.  Add an `implementation` to your `dependencies` section.

    ```groovy
    implementation 'net.kaoriya:extractarticle:0.0.1'
    ```

Please read [public document](https://docs.github.com/en/packages/guides/configuring-gradle-for-use-with-github-packages) also. ([Japanese](https://docs.github.com/ja/packages/guides/configuring-gradle-for-use-with-github-packages)).

## Getting Started

To be written.

```java
import net.kaoriya.extractarticle.ArticleExtractor;

var r = ArticleExtractor.extract(new java.io.File("./foobar.html"));

// print the article (main contents). [String]
System.out.println(r.text); 

// print the description (meta[name='description']). [String]
System.out.println(r.desc);

// print the score: probability of main contents. [double: 0.0~1.0, NaN]
System.out.println(Double.toString(r.score));
```

### Score

Extracted article may be wrong depending on HTML contents.  To verify the
extraction succeeded or not, this extractor provides score for it.

The score is calculated by considering the description is came from the
article. In other words: how many ratio of description can be found in the
article.  So it will be very close to 1.0 if better extraction done.

(in Japanese)

`extract()` は稀に本文ではないところを抽出して失敗します。
そのため「descriptionは本文を再利用していることが多い」ことを利用して
descriptionと本文の類似度を得点化することで、
本文抽出がどの程度成功しているかを見れるようにしたのが `score` フィールドです。

この得点は次のように計算しています。
これはつまりdescriptionが本文中にどの程度含まれているかを示しています。

```
count({descの2-gram index} ∩ {本文の2-gram index})
--------------------------------------------------
            count({descの2-gram index})
```

この `score` がおよそ 0.8 から 0.9 を超えていれ
ほぼ間違いなく正しく本文を抽出できたと考えられます。

またこの判定は以下のようなケースでは正しく機能しません。
しかしそのようなコンテンツは稀であるか、
あっても写真しかないブログのように本文に解析価値を見出しにくいケースであるため、
本ライブラリでは救済策を用意していません。

* そもそもdescriptionが設定されてない
* descriptionに本文とは関係ない文章が設定されている

### `extract()` variations

`ArticleExtractor.extract()` has some variations.
Most basic form is `extract(org.jsoup.nodes.Document doc)`.

Other variations like this:

* `extract(java.lang.String html)`.
* `extract(java.io.File file) throws java.io.IOException`.
* `extract(java.io.InputStream in) throws java.io.IOException`

## How to release

1. update `version` in build.gradle
2. `./gradlew test`
3. `./gradlew publish`

    Set these properties with correct values in ~/.gradle/gradle.properties

    ```props
    gpr.user=
    gpr.key=
    ```
