package contracts.payment_service

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name("Create Payment")
    description("Verify that a Payment is generated")

    input {
        messageFrom 'contract-test.exchange'
        messageBody ([
                title: "Clockwork Orange"
        ])
        messageHeaders {
            messagingContentType(applicationJson())
        }
        bodyMatchers {
            jsonPath('''$.title''', byEquality())
        }
        assertThat("messageIsValid()")
    }

}