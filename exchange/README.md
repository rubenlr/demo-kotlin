# Coding challange

## Task

For this task you need to use Spring Boot with Kotlin and Postgres database as this is our main
stack.
The goal of this task is to develop a rest application that will be mimicking exchange
platform that will enable functionality via API:
1. Users can deposit and withdraw funds of their balance/account.
2. Users can buy assets using the balance in their account. Assets have 3 different types,
Crypto (BTC, ETH), Stocks (Apple, Tesla), Commodities (Gold, Silver).
3. Users can sell assets, and their balances are updated accordingly - updates the FIAT
(EUR) account balance.
4. Users can get the balance of any certain assets.
5. **Extra**. Support of conversion from one asset to another

## Remarks

- User accounts can be mocked or created via migration, no registration and
authentication are necessary.
- Regarding prices of the assets, feel free to use random prices, as in 1 APPLE stock is
100 EUR, it can be hard coded or used from a random generator when asking for the
price. Can also use external sources, but not mandatory.
- Feel free to add a small explanation (it does not have to be in the code) about metrics
you would collect to monitor performance of your service.
- **Bonus**. We would also love to see some tests. Feel free to use testcontainers and other
testing frameworks.
- **Bonus**. We expect to receive a working application that can be run using a single
command, so if in the end the application will have docker compose with all necessary
service it would be good.