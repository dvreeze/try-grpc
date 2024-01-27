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

package eu.cdevreeze.trygrpc.primefactors.client

import eu.cdevreeze.trygrpc.proto.PrimeFactorsServiceGrpc
import eu.cdevreeze.trygrpc.proto.PrimeFactorsServiceOuterClass.{GetPrimeFactorsRequest, GetPrimeFactorsResponse}
import io.grpc.{ManagedChannel, ManagedChannelBuilder}

import scala.jdk.CollectionConverters.ListHasAsScala

/**
 * Program running a PrimeFactorsService client.
 *
 * @author
 *   Chris de Vreeze
 */
object PrimeFactorsClientRunner:

  private val port: Int = System.getProperty("grpc.port", "9090").toInt

  def main(args: Array[String]): Unit =
    require(args.lengthIs == 1, s"Usage: PrimeFactorsClientRunner <input number>")
    val inputNumber = BigInt(args(0))

    val managedChannelBuilder: ManagedChannelBuilder[?] = ManagedChannelBuilder.forAddress("localhost", port)
    // TODO Configure
    val managedChannel: ManagedChannel = managedChannelBuilder.usePlaintext().build()

    val stub: PrimeFactorsServiceGrpc.PrimeFactorsServiceBlockingStub = PrimeFactorsServiceGrpc.newBlockingStub(managedChannel)

    val request: GetPrimeFactorsRequest =
      GetPrimeFactorsRequest.newBuilder().setInputNumber(inputNumber.toLong).build()

    val response: GetPrimeFactorsResponse = stub.getPrimeFactors(request)
    val primeFactors: Seq[Long] = response.getPrimeFactorsList.asScala.toSeq.map(Long.unbox)

    assert(primeFactors.ensuring(_.nonEmpty).product == inputNumber.toLong)

    println(s"Prime factors of $inputNumber: ${primeFactors.mkString(", ")}")
  end main

end PrimeFactorsClientRunner
