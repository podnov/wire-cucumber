# wire-cucumber

[<img src="https://github.com/podnov/wire-cucumber/workflows/java-ci/badge.svg">](https://github.com/podnov/wire-cucumber/actions?query=workflow%3A%22java-ci%22)

The intent of this project is to make mocking external REST calls easier by standardizing how how the mocks are configured and reducing the scaffolding needed to create those mocks.

An example cucumber feature consuming the features of this library can be [found in this project](src/test/resources/com/evanzeimet/wirecucumber/wire-cucucmber.feature). The steps defined in this test for this project only define a [few steps](src/test/java/com/evanzeimet/wirecucumber/WireCucucmberTest.java). The steps that create the REST mocks are defined [in the library itself](src/main/java/evanzeimet/wirecucumber/WireCucucmberSteps.java) during step initialization.