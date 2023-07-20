# Swede jvm

## Usage:

Create feature file in test resource dir (feature/Calculator.swede):

```plain
@example
Feature: calculator

# comment

@positive
Scenario: Test addition
- Add "2" and "2"
- Check that result is "4"
```

Create step implementation class (CalculatorStepImpl.java):

```java
public class CalculatorStepImpl {
    @Step("Add <a> and <b>")
    public void addTwoNumbers(int a, int b, ScenarioContext scenarioContext) {
        System.out.println("Adding " + a + " and " + b);
        scenarioContext.put("result", a + b);
    }

    @Step("Check that result is <value>")
    public void checkThatResultIs(int value, ScenarioContext scenarioContext) {
        System.out.println("Checking that result is " + value);
        assertEquals(value, scenarioContext.get("result"));
    }
}
```

Create empty test with SwedeTest annotation(SwedeRunTest.java):

```java

@SwedeTest(featureDirectory = "feature", stepImplClasspath = "org.swede.demo.steps")
class SwedeRunTest {
}
```

JUnit5 will run all tests from the specified directory.

For more details see swede-demo-project.
