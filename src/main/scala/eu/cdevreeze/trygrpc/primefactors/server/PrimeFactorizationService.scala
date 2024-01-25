/*
 * Copyright 2024-2024 Chris de Vreeze
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.cdevreeze.trygrpc.primefactors.server

import scala.annotation.tailrec

/**
 * Prime factorization service.
 *
 * @author
 *   Chris de Vreeze
 */
trait PrimeFactorizationService:

  def findPrimeFactors(n: BigInt): Seq[BigInt]

object PrimeFactorizationService:

  private val zero = BigInt(0)
  private val one = BigInt(1)
  private val two = BigInt(2)
  private val three = BigInt(3)
  private val five = BigInt(5)
  private val six = BigInt(6)
  private val nine = BigInt(9)
  private val ten = BigInt(10)

  private val divBy2Digits: Set[Char] = Set('0', '2', '4', '6', '8')
  private val divBy5Digits: Set[Char] = Set('0', '5')

  object DefaultPrimeFactorizationService extends PrimeFactorizationService:

    override def findPrimeFactors(n: BigInt): Seq[BigInt] = {
      if n < two then sys.error(s"Expected a number >= 2") else findPrimeFactors(n, two, Nil).reverse
    }

    @tailrec
    private def findPrimeFactors(n: BigInt, nextPotentialFactor: BigInt, acc: List[BigInt]): List[BigInt] =
      if n == one then acc
      else if skipAsRoot(nextPotentialFactor) then findPrimeFactors(n, nextPotentialFactor + 1, acc)
      else if !isDivisibleBy(n, nextPotentialFactor) then findPrimeFactors(n, nextPotentialFactor + 1, acc)
      else
        // The algorithm ensures that nextPotentialFactor is a prime number if we can divide n by it, proven below by reduction ad absurdum.
        // Suppose that nextPotentialFactor is non-prime, and we could divide n by nextPotentialFactor, once or more. Then the prime
        // factors of nextPotentialFactor (once or more) would be smaller than nextPotentialFactor and we would already have divided
        // n by all those prime factors. The GCD (greatest common divisor) of the resulting value of n and nextPotentialFactor
        // would then be 1. So if n is divisible by nextPotentialFactor, nextPotentialFactor must be a prime number.
        // So the prime factors we find are indeed all prime numbers.
        findPrimeFactors(n / nextPotentialFactor, nextPotentialFactor, nextPotentialFactor :: acc)
    end findPrimeFactors

    private def isDivisibleBy(n: BigInt, m: BigInt): Boolean = n % m == zero

    private def skipAsRoot(n: BigInt): Boolean =
      isRealMultipleOf2(n) || isRealMultipleOf5(n) || isRealMultipleOf3(n)

    private def isRealMultipleOf2(n: BigInt): Boolean =
      n != two && n.toString.lastOption.exists(c => divBy2Digits.contains(c))

    private def isRealMultipleOf5(n: BigInt): Boolean =
      n != five && n.toString.lastOption.exists(c => divBy5Digits.contains(c))

    private def isRealMultipleOf3(n: BigInt): Boolean =
      n != three && isMultipleOf3(n)

    @tailrec
    private def isMultipleOf3(n: BigInt): Boolean =
      if n < ten then n == three || n == six || n == nine
      else
        val digitSum = sumOfDigits(n)
        // Recursion
        isMultipleOf3(digitSum)

    private def sumOfDigits(n: BigInt): BigInt =
      n.toString.filter(_.isDigit).flatMap(_.toString.toIntOption).sum

  end DefaultPrimeFactorizationService

end PrimeFactorizationService
