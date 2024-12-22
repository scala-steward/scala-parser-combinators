/*
 * Scala (https://www.scala-lang.org)
 *
 * Copyright EPFL and Lightbend, Inc. dba Akka
 *
 * Licensed under Apache License 2.0
 * (http://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */

import scala.util.parsing.input._

import org.junit.Test
import org.junit.Assert.fail

class t8879 {

  @Test
  def test: Unit = {
     val testPagedSeq = {
       var nbpage = 0
       def more(data: Array[Char], start: Int, len: Int): Int = {
         if (nbpage < 1) {
           var i = 0
           while (i < len && nbpage < 3) {
             if (i % 100 != 0) {
               data(start + i) = 'a'
             } else {
               data(start + i) = '\n'
             }
             i += 1
           }
           if (i == 0) -1 else {
             nbpage += 1
             i
           }
         } else {
           fail("Should not read more than 1 page!")
           0
         }
       }

       new PagedSeq(more(_: Array[Char], _: Int, _: Int))
     }

     val s = new StreamReader(testPagedSeq, 0, 1)

     // should not trigger reading of the second page
     s.drop(20)
  }
}
