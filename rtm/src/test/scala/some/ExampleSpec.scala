package some

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable

class ExampleSpec extends AnyFlatSpec with Matchers {

  "A Stack" should "pop values in last-in-first-out order | CSW-65437" in {
    val stack = new mutable.Stack[Int]
    stack.push(1)
    stack.push(24)
    stack.pop() should be(24)
    stack.pop() should be(1)
  }

  it should "throw NoSuchElementException if an empty stack is popped | CSW-7485" in {
    val emptyStack = new mutable.Stack[Int]
    a[NoSuchElementException] should be thrownBy {
      emptyStack.pop()
    }
  }
}
