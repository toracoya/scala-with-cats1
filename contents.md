# Scala with Cats
# 定期輪読会

### Chapter.1 Introduction

</br>

2018-11-15

@orepuri

---

## Chapter1. Introduction

---

<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  Chapter1. Introduction
</div>

### Catsとは?

</br>

https://typelevel.org/cats/

Scalaで関数型プログラミングするためのライブラリ

いろいろな種類のライブラリがあり好きなものを選んで使う

---

<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  Chapter1. Introduction
</div>

### 型クラス

</br>

主なものは*型クラス*として提供され, 既存のScalaの型に適用される

Haskell由来

継承やオリジナルのコードを変更せずに機能追加できる

---

<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  Chapter1. Introduction
</div>

### 本章の概要

</br>

型クラスについて

2つの型クラス*Show*と*Eq*

型クラスの基礎となるパターン

---

<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.1 Anatomy of a Type Class
</div>

#### 型クラスの重要な3つのパターン

* 1. 型クラス

* 2. 型クラスのインスタンス

* 3. 型クラスを使うためのインタフェース

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.1 Anatomy of a Type Class
</div>

#### 1. 型クラス

</br>

必要とする機能を表現するインターフェース/API

Catsでは1つ以上の型パラメータを持つtraitとして表現

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.1 Anatomy of a Type Class
</div>

#### 例: JSONのシリアライズ

```scala
// JSON AST
sealed trait Json
final case class JsObject(get: Map[String, Json]) extends Json
final case class JsString(get: String) extends Json
final case class JsNumber(get: Double) extends Json
final case object JsNull extends Json

// オブジェクトをJSONにシリアライズする型クラス
trait JsonWriter[A] {         // <- Aは機能を提供したい型
  def write(value: A): Json   // <- writeはAに提供する機能. 型によって実装が変わる
}
```

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.1 Anatomy of a Type Class
</div>

#### 2. 型クラスのインスタンス

</br>

特定の型に対する実装

Scalaの標準ライブラリやドメインモデルの型など

型クラスの実装を作りimplicitキーワードを付与


</br>
---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.1 Anatomy of a Type Class
</div>

#### 例: JsonWriterのインスタンス

```scala
final case class Person(name: String, email: String)

object JsonWriterInstances {
  // 型Stringに対する型クラスJsonWriterのインスタンス
  implicit val stringWriter: JsonWriter[String] = new JsonWriter[String] {
    def write(value: String): Json = JsString(value)
  }
  // 型Personに対する型クラスJsonWriterのインスタンス
  implicit val personWriter: JsonWriter[Person] = new JsonWriter[Person] {
    def write(value: Person): Json = JsObject(Map(
      "name" -> JsString(value.name),
      "email" -> JsString(value.email),
    ))
  }
}
```

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.1 Anatomy of a Type Class
</div>

#### 3. 型クラスを使うためのインタフェース

</br>

ユーザーに提供したい(特定の型に対する)機能

implicitパラメータで型クラスのインスタンスを受け取る</br>
ジェネリックなメソッド

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.1 Anatomy of a Type Class
</div>

#### 型クラスのインタフェースを特定する方法

</br>

オブジェクトインタフェース(Interface Objects)

シンタックスインタフェース(Interface Syntax)

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.1 Anatomy of a Type Class
</div>

#### オブジェクトインタフェース

```scala
object Json {
  // シングルトンオブジェクトに型クラスを使うためのインタフェースを定義
  def toJson[A](value: A)(implicit w: JsonWriter[A]): Json = {
    w.write(value)
  }
}
```

```scala
import JsonWriterInstances._   // To use JsonWriter[Person]

Json.toJson(Person("Dave", "dave@example.com"))
// Json.toJson(Person("Dave", "dave@example.com"))(personWriter)
```


---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.1 Anatomy of a Type Class
</div>

#### シンタックスインタフェース

```scala
object JsonSyntax {
  implicit class JsonWriterOps[A](value: A) {
    def toJson(implicit w: JsonWriter[A]): Json = w.write(value)
  }
}
```

```scala
import JsonWriterInstances._ // To use JsonWrtier[Person]
import JsonSyntax._          // To use JsonWriterOps[Person]

Person("Dave", "dave@example.com").toJson
// JsonWriterOpts(Person("Dave", "dave@example.com"))
//   .toJson(personWriter)
```

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.1 Anatomy of a Type Class
</div>

#### Implicitlyメソッド

</br>

スコープ中のimplicitな値を明らかにする

デバッグに便利

```
def implicitly[A](implicit value: A): A = value
```

```
import JsonWriterInstances._

implicitly[JsonWrinter[String]]
// JsonWriter[String] = JsonWriterInstances$$anon$1@73eb1c7a

implicitly[JsonWrinter[String]] == stringWriter
// true
```

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.1 Anatomy of a Type Class
</div>

#### 

</br>











---

型クラス, クラス, インターフェースについて
共通点, 違い