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

import eu.cdevreeze.trygrpc.proto.PrimeFactorsServiceGrpc
import eu.cdevreeze.trygrpc.proto.PrimeFactorsServiceOuterClass.{GetPrimeFactorsRequest, GetPrimeFactorsResponse}
import io.grpc.stub.StreamObserver

import scala.jdk.CollectionConverters.SeqHasAsJava

/**
 * Prime factors gRPC service implementation.
 *
 * @author
 *   Chris de Vreeze
 */
final class PrimeFactorsServiceImpl(val primeFactorizationService: PrimeFactorizationService)
    extends PrimeFactorsServiceGrpc.PrimeFactorsServiceImplBase:

  override def getPrimeFactors(request: GetPrimeFactorsRequest, responseObserver: StreamObserver[GetPrimeFactorsResponse]): Unit =
    val primeFactors: Seq[java.lang.Long] =
      primeFactorizationService.findPrimeFactors(request.getInputNumber).map(_.toLong).map(Long.box)

    val response: GetPrimeFactorsResponse = GetPrimeFactorsResponse.newBuilder().addAllPrimeFactors(primeFactors.asJava).build()

    responseObserver.onNext(response)
    responseObserver.onCompleted()
  end getPrimeFactors

end PrimeFactorsServiceImpl
