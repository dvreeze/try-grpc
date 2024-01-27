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

import io.grpc.protobuf.services.{HealthStatusManager, ProtoReflectionService}
import io.grpc.{BindableService, Server, ServerBuilder}

import scala.concurrent.ExecutionContext
import scala.jdk.CollectionConverters.SeqHasAsJava

/**
 * Program running PrimeFactorsService.
 *
 * @author
 *   Chris de Vreeze
 */
object PrimeFactorsServiceRunner:

  private val port: Int = System.getProperty("grpc.port", "9090").toInt

  def main(args: Array[String]): Unit =
    val primeFactorsServiceImpl: PrimeFactorsServiceImpl =
      PrimeFactorsServiceImpl(PrimeFactorizationService.DefaultPrimeFactorizationService)

    val healthStatusManager: HealthStatusManager = new HealthStatusManager()

    // See https://github.com/grpc/grpc-java/blob/master/documentation/server-reflection-tutorial.md
    // Also see https://github.com/fullstorydev/grpcurl (for gRPCurl)
    val protoReflectionService: BindableService = ProtoReflectionService.newInstance()

    val bindableServices: Seq[BindableService] =
      Seq(primeFactorsServiceImpl, healthStatusManager.getHealthService, protoReflectionService)

    val serverBuilder: ServerBuilder[?] = ServerBuilder.forPort(port)
    val server: Server =
      serverBuilder
        .executor(ExecutionContext.global)
        .addServices(bindableServices.map(_.bindService()).asJava)
        .build()
    // Add shutdown hook
    // Add useful interceptors

    server.start()
    server.awaitTermination()
  end main

end PrimeFactorsServiceRunner
