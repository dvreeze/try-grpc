========
Try-gRPC
========

This project tries out basic usage of gRPC_, in particular gRPC-Java_.

With a gRPC server running, we can use the grpcurl_ command line tool to "reflect" on it.
We could then invoke an RPC (after finding out reflectively how to do so). For example:

.. code-block:: bash

    ~/go/bin/grpcurl -plaintext localhost:9090 list
    ~/go/bin/grpcurl -plaintext localhost:9090 list \
      eu.cdevreeze.trygrpc.proto.PrimeFactorsService
    ~/go/bin/grpcurl -plaintext localhost:9090 describe \
      eu.cdevreeze.trygrpc.proto.PrimeFactorsService.GetPrimeFactors
    ~/go/bin/grpcurl -plaintext localhost:9090 describe \
      eu.cdevreeze.trygrpc.proto.GetPrimeFactorsRequest
    ~/go/bin/grpcurl -plaintext localhost:9090 describe \
      eu.cdevreeze.trygrpc.proto.GetPrimeFactorsResponse
    # Running an RPC
    ~/go/bin/grpcurl -plaintext -d '{ "input_number": 93583350 }' localhost:9090 \
      eu.cdevreeze.trygrpc.proto.PrimeFactorsService.GetPrimeFactors

.. _gRPC: https://grpc.io/
.. _gRPC-Java: https://grpc.io/docs/languages/java/quickstart/
.. _grpcurl: https://github.com/fullstorydev/grpcurl
