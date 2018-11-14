# Scala with Cats
# 定期輪読会

### Chapter.1 Introduction

</br>

2018-11-29

@orepuri

---

## Chapter1. Introduction

---

<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  Chapter1. Introduction
</div>

### Catsとは?

</br>

Scalaで関数型プログラミングするためのライブラリ

いろいろな種類のライブラリがあり好きなものを選んで使う

https://typelevel.org/cats/

---

<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  Chapter1. Introduction
</div>

### 型クラス

</br>

Catsの主な機能は*型クラス*として提供

既存のScalaの型に適用される

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

1. 型クラス

2. 型クラスのインスタンス

3. 型クラスを使うためのインタフェース

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

// JSONにシリアライズする機能を提供する型クラス. 型パラメータが1つ
trait JsonWriter[A] {            // Aは機能を提供したい型
  def write(value: A): Json   // Aに対して提供する機能. 型によって実装が変わる
}
```

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.1 Anatomy of a Type Class
</div>

#### 2. 型クラスのインスタンス

</br>

Scalaの標準ライブラリやドメインモデルの型など</br>
特定の型に対する実装

型クラスの実装を作りimplicitキーワードを付与

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

#### 3. 型クラスを使うための方法(インタフェース)

</br>

ユーザーに提供したい(特定の型に対する)機能

implicitパラメータで型クラスのインスタンスを受け取る</br>
ジェネリックなメソッド

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.1 Anatomy of a Type Class
</div>

#### 型クラスの機能を提供する方法(インタフェース)

</br>

Interface Objects: オブジェクトに定義

Interface Syntax: 便利なシンタックスを提供

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.1 Anatomy of a Type Class
</div>

#### オブジェクトに定義する方法

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

#### 便利なシンタックスを提供する方法

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
  1.2 Working with Implicits
</div>

Scalaで型クラスを使う

=> implicitな値とimplicitパラメータを使う

</br>

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.2 Working with Implicits
</div>

#### implicitの配置

</br>

implicitの定義はobjectかtraitの中に配置しなければならない

トップレベルには定義できない

前節の例ではobject JsonWriterInstancesに配置

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.2 Working with Implicits
</div>

#### implicitの配置

</br>

JsonWriterのコンパニオンオブジェクトの中に配置することもできる

コンパニオンオブジェクトの中に配置することは</br>
暗黙のスコープと呼ばれる特別な意味を持つ

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.2 Working with Implicits
</div>

#### 暗黙のスコープ(Implicit Scope)

</br>

コンパイラは型によって型クラスのインスタンスの候補を探す

```scala
// Json.toJson[A](value: A)(implicit w: JsonWriter[A])
//   => Json.toJson[String](value: String)(implicit w: JsonWriter[String])
//   => JsonWriter[String]を探す
Json.toJson("A string!")
```

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.2 Working with Implicits
</div>

#### 暗黙のスコープからの探索(簡易ルール)

</br>

ローカルもしくは継承された定義

importされた定義

型クラスのコンパニオンオブジェクト, または型パラメータ

<div style="font-size: 50%;">
JsonWriter[String]の場合</br>
JsonWriterのコンパニオンオブジェクトと型パタメータStringの定義から
</div>

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.2 Working with Implicits
</div>

#### 注意点

implicitキーワードが付与されていること

重複する候補がないこと

</br>


---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.2 Working with Implicits
</div>

#### Implicit配置方法

</br>

1. JsonWriterInstancesのようにobjectに配置 (importする)

2. traitに配置 (継承する)

3. 型クラスのコンパニオンオブジェクトに配置 (暗黙のスコープ)

4. 型パラメータのコンパニオンオブジェクトに配置 (暗黙のスコープ)


---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.2 Working with Implicits
</div>

#### 再帰的なImplicitの解決

</br>

既存の型クラスのインスタンスを使って</br>
他のインスタンスを生成できる

インスタンスの定義方法は2つ

1. implicitな変数として

2. implicitな関数として

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.2 Working with Implicits
</div>

#### 例: OptionのJsonWriter 

```scala
implicit val optionIntWriter: JsonWriter[Option[Int]] = ???

implicit val optionPersonWriter: JsonWriter[Option[Person]] = ???
```

implicit valで定義するとOption[A]のすべてのAに対して定義が必要


---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.2 Working with Implicits
</div>

#### 例: OptionのJsonWriter

```scala
// implicitパラメータがないimplicit defは暗黙の変換(implicit conversion)と呼ばれ
// 警告がでる
implicit def optionWriter[A](
  implicit writer: JsonWriter[A]): JsonWriter[Option[A]] = {

  new JsonWriter[Option[A]] {
    def write(option: Option[A]): Json = {
      option match {
        case Some(value) => writer.write(value)
        case None => JsNull
      }
    }
  }
}
```

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.3 Exercise: Printable Library
</div>

#### Exercise: Printable Library

省略

</br>

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.4 Meet Cats
</div>

#### Catsの型クラス

どのように実装されているか*Show*を例に見る

```scala
package cats

trait Show[A] {
  def show(value: A): String
}
```

</br>

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.4 Meet Cats
</div>

#### 型クラスのimport

```scala
import cats.Show

val showInt = Show.apply[Int]
// <console>:12: error: could not find implicit value for parameter
//  instance: cats.Show[Int]
//       val showInt = Show.apply[Int]
```

Show.applyはここのインスタンスが必要

```scala
object Show {
  def apply[A](implicit instance: Show[A]): Show[A] = instance
  ...
}
```

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.4 Meet Cats
</div>

#### インスタンスのimport

`cats.instances` パッケージにいろいろな型の</br>
デフォルトインスタンスがある

`cats.instances.int`

`cats.instances.string`

`cats.instances.list`

`cats.instances.option`

`cats.instances.all`

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.4 Meet Cats
</div>

#### インスタンスのimport

```scala
import cats.instances.int._  // for Show[Int]
import cats.instances.string._  // for Show[String]

val showInt: Show[Int] = Show.apply[Int]
val showString: Show[String] = Show.apply[String]

showInt.show(123)
showString.show("abc")
```

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.4 Meet Cats
</div>

#### シンタックスインタフェースのimport

</br>

```scala
import cats.syntax.show._

val showInt = 123.show
```

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.4 Meet Cats
</div>

#### すべてのimport

</br>

```scala
// catsの全型クラス
import cats._

// 標準ライブラリの全型クラスのインスタンス
import cats.instances.all._

// すべてのシンタックス
import cats.syntax.all._

// すべての標準型クラスのインスタンスとシンタックス
import cats.implicits._
```

```scala
// Catsのすべての型クラス, インスタンス, シンタックス
import cats._
import cats.implicits._
```

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.4 Meet Cats
</div>

#### 独自の型クラスのインスタンスを定義

</br>

```scala
import java.util.Date

implicit val dateShow: Show[Date] = new Show[Date] {
  def show(date: Date): String =
    s"${date.getTime}ms since the epoch."
}
```

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.4 Meet Cats
</div>

#### より簡単なインスタンスの定義

</br>

```
object Show {
  // A => Stringの関数を使ってShow[A]を生成
  def show[A](f: A => String): Show[A] = ...

  // toStringを使ってShow[A]を生成
  def fromToString[A]: Show[A] = ...
}
```

```
implicit val dateShow: Show[Date] = Show.show(date => s"${date.getTime}ms since the epoch.")

implicit val dateShow: Show[Date] = Show.fromToString[Date]
```

Show以外の型クラスもインスタンスを作るための</br>
ヘルパーメソッドを提供


---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.4 Meet Cats
</div>

#### なぜShowが必要か

</br>

toStringでいいのでは?

toStringはAnyObjectで定義されており</br>
すべてのクラスが独自の定義を持っている

Scalaの標準クラスやサードパーティーのクラスの</br>
toStringを直接変更することはできない

Show型クラスを使えばできる

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.5 Example: Eq
</div>

#### 例: cats.Eq

</br>

`cats.Eq`

型安全な等価を提供


---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.5 Example: Eq
</div>

#### Scalaの==は型安全ではない

```scala
List(1, 2, 3).map(Option(_)).filter(item => item == 1)
// res0: List[Option[Int]] = List()
```

デフォルトで`==`はJavaのObject#equalsを使う

```scala
class Object {
  ...
  // 引数がObjectなので任意の型と比較ができる
  def equals(that: Object): Boolean
  ...
}
```

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.5 Example: Eq
</div>

#### Eq

```scala
package cats

trait Eq[A] {
  // 同じ型のみで比較できる
  def eqv(a: A, b: A): Boolean
}
```

```scala
import cats.instances.int._ // for Eq[Int]

val eqInt = Eq[Int]
eqInt.eqv(123, 123) // true
eqInt.eqv(123, 234) // false

eqInt.eqv(123, "234") // compile error
```
</br>

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.5 Example: Eq
</div>

#### Eqのシンタックス

```
import cats.syntax.eq._ // for === and =!=

123 === 123 // true
123 =!= 234 // true

123 =!= "234" // compile error
```

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.5 Example: Eq
</div>

#### Optionの比較

```scala
import cats.instances.int._     // for Eq[Int]
import cats.instances.option._  // for Eq[Option[A]]

Some(1) === None // compile error
```

Option[Int]のインスタンスはあるがSome[Int]のインスタンスはない

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.5 Example: Eq
</div>

#### Optionの比較


```scala
(Some(1): Option[Int]) === (None: Option[INT]) // false

// more better
Option(1) === Option.empty[Int]
```

```scala
import cats.syntax.option._  // for some and none

1.some === none[Int]
1.some =!= none[Int]
```

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.5 Example: Eq
</div>

#### 独自型の比較

```scala
import java.util.Date
import cats.instances.long._ // for Eq[Long]

// Eqが(A, A) => Boolean からインスタンスを生成するヘルパーメソッドを提供
implicit val dateEq: Eq[Date] = Eq.instance[Date] { (date1, date2) =>
  // Eq[Long]を使用
  date1.getTime === date2.getTime 
}
```

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.6 Controlling Instances Selection
</div>

#### インスタンスの選択で注意すること

</br>

ある型のインスタンスと, その型のサブ型の関係

<div style="font-size: 50%;">
例: JsonWriter[Option[Int]] と JsonWriter[Some[Int]]
</div>

複数のインスタンス候補があるときにどれを選ぶべきか

<div style="font-size: 50%;">
例: JsonWriter[Person]のインスタンスが2つある場合
</div>


---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.6 Controlling Instances Selection
</div>

#### 変位指定(Variance)

</br>

型クラスを定義するとき, 変位アノテーションを追加することができる

共変

反変

非変


---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.6 Controlling Instances Selection
</div>

#### 共変(Covariance)

```scala
trait F[+A]
```

BがAのサブ型の場合, F[B]がF[A]のサブ型となる

```scala
trait List[+A]

sealed trait Shape
case class Circle(radius: Double) extends Shape

val circles: List[Circle] = ...
val shapes: List[Shape] = circles  // List[Circle] は List[Shape] のサブ型
```


---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.6 Controlling Instances Selection
</div>

#### 反変(Contravariance)

```scala
trait F[-A]
```

BがAのサブ型の場合, F[A]がF[B]のサブ型となる</br>
何かを処理する型のモデリングに使うことが多い


```scala
trait JsonWriter[-A] {
  def write(value: A): Json
}

val shape: Shape = ...
val circle: Circle = ...

val shapeWriter: JsonWriter[Shape] = ...
val circleWriter: JsonWriter[Circle] = ...

def format[A](value: A, writer: JsonWriter[A]): Json = writer.write(value)
```


---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.6 Controlling Instances Selection
</div>

#### 反変(Contravariance)

```scala
def format[A](value: A, writer: JsonWriter[A]): Json = writer.write(value)
```

渡すことができるvalueとwriterの組み合わせはどれか?

```scala
// OK
format(shape, shapeWriter)
format(circle, circleWriter)

// OK: CircleはShapeなので, ShapeWriterでも使える
format(circle, shapeWriter)

// NG: ShapeはCircleとは限らない.
// Circle以外のShapeに対してCircleWriterは使えない
format(shape, circleWriter) 

```

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.6 Controlling Instances Selection
</div>

#### 反変(Contravariance)

</br>

CircleはShapeのサブ型

JsonWriter[Shape]はJsonWriter[Circle]のサブ型とする（反変）

JsonWriter[Circle]として使いたいすべての場所で</br>
代わりにJsonWriter[Shape]を使うことができる

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.6 Controlling Instances Selection
</div>

#### 反変(Contravariance)

</br>

JsonWriter[Shape] = (Circleを含む)Shapeを受け取ってJsonにする

JsonWriter[Circle] = Circleを受け取ってJsonにする</br>

JsonWriter[Circle]の代わりにJsonWriter[Shape]が使える

JsonWriter[Shape]がJsonWriter[Circle]のサブ型となる

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.6 Controlling Instances Selection
</div>

#### 非変(invariance)

```scala
trait F[A]
```

F[A]とF[B]はどちらかのサブ型にならない


---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.6 Controlling Instances Selection
</div>

#### implicitの検索

</br>

コンパイラは型, もしくはそのサブ型に1つだけマッチするimplicitを探す

よって, 型クラスで変位指定することで選ばれる</br>インスタンスを制御することができる

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.6 Controlling Instances Selection
</div>

#### 問題点

</br>

```scala
sealed trait A
final case object B extends A
final case object C extends A
```

Aのためのインスタンスを定義し, それをBとCでも使うことができるか

AとBのためのインスタンスがあり, かつBの値があるとき</br>
AではなくBのためのインスタンスが選択できるか

同時に両方を満たすことはできない

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.6 Controlling Instances Selection
</div>

#### 変位指定の選択

Type Class Variance|Invariant|Covariant|Contravariant
--|--|--|--
Supertype instance used?|No|No|Yes
More specific type preferred?|No|Yes|No


---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.6 Controlling Instances Selection
</div>

#### Catsの変位指定

</br>

Catsの型クラスは一般的に非変

Optionのための型クラスはSomeのために使えない


---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  1.7 Summary
</div>

### 1章まとめ

</br>

型クラス

Catsの型クラスのパターン

型クラス, インスタンス, インタフェース（型クラスを使うための）

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  補足
</div>

#### 型クラス

何かしらの機能を提供するもの

Show, Eq, Monoid, Semigroups, Functor, Monad, ...

#### 型クラスのインスタンス

ある型に対して型クラスが提供する機能を実装したもの

Show[Int], Eq[String], Monoid[Int]

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  補足
</div>

#### 型クラス

Scalaの標準のOptionやListはtraitで定義されていて</br>
型パラメータも受け取るが型クラスか?

No: 型パラメータで指定される型によって実装が変わらない

Option[Int]でもOption[String]でも実装が変わらない

JsonWriter[Int]とJsonWriter[String]では実装が違う


---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  補足
</div>

#### 型クラスとOOPのクラス

</br>

OOPのクラス

値(インスタンス)を生成するためのテンプレート</br>
クラスが型にもなる

型クラス

ある型に対して機能を追加するもの</br>
型クラス自体は型ではない</br>
Show, Eq, Monoidは型ではない</br>
Show[Int], Eq[Person]は型になる


---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  補足
</div>

#### 型クラスとOOPのインタフェース

OOPのインタフェース

継承によりクラスに機能を追加する</br>
インタフェースが型にもなる</br>
クラス定義で継承して実装しないといけない

型クラス

Implicitにより型に機能を追加する(Scala)</br>
型クラス自体は型ではない</br>
クラス定義とは別に機能を追加することができる</br>
(アドホック多相)



</br>

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  補足
</div>

#### Scalaでの関数型プログラミングを学ぶ理由

</br>

シンプルで型安全, 拡張性が高いコードが書ける

Scala自体やライブラリのコードを理解しやすくなる</br>
(型クラスの理解というより高カインド型, implicit)

Scalaを採用している多くの企業で普通に使ってる</br>
(サイバー, セプテーニ, ChatWorks, Fringe, etc...)

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  補足
</div>

#### Scala with Cats

</br>

コード例が多く, 英語も比較的読みやすい

実用的に学習できる

理論的な知識はあまりつかない


---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  補足
</div>

#### 今後学ぶこと

</br>

Monoid, Semigroups, Functor, Monad, State, Reader, Writer, Monad Transformer, Applicative, ...etc

すべてはComposability(合成可能性)のため

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  補足
</div>

#### 今後学ぶ上で持つとよい視点

</br>

何のためにあるのか, 何が便利になるのか

既存コードで実装できそうな(使ったほうがよい)機能はあるか

どういう処理を実装するのに使えるのか

どうやって実装されているか(Catsのコード)

---
<div style="text-align: right; font-size: 15px; padding-bottom: 50px;">
  補足
</div>

#### Show型クラスの使用例

BigDecimal#toString

```scala
scala> val cpm = BigDecimal("0.000000123")
cpm: scala.math.BigDecimal = 1.23E-7

scala> cpm.toString
res0: String = 1.23E-7 // 標準のtoStringでは指数表記になる

scala> cpm.bigDecimal.toPlainString // めんどくさい
res1: String = 0.000000123

scala> implicit val bigDecimalShow: Show[BigDecimal] = Show.show(_.bigDecimal.toPlainString)
bigDecimalShow: cats.Show[BigDecimal] = cats.Show$$anon$1@4fbb52f6

scala> cpm.show
res2: String = 0.000000123 // Nice!

```

実際は金額用のドメインモデルを作りましょう...