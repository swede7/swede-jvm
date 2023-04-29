Feature: Basic calculator operations

Description: This feature defines a set of operations that the calculator must support.

Tags: all

# Comment example

Precondition:
- Turn on calculator

Scenario: Addition
Tags:  pass, automated
- Enter "2 + 2"
- Click on calculation button
- Check that the answer is "5"

Scenario: Division by zero
Tags: fail
- Enter "5 / 0"
- Click on calculation button
- Аn exception must be thrown
