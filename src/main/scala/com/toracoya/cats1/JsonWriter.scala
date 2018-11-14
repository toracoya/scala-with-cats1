package com.toracoya.cats1

trait JsonWriter[A] {            // Aは機能を提供したい型
  def write(value: A): Json   // Aに対して提供する機能. 型によって実装が変わる
}
