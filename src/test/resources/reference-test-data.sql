insert into customer (id, name, status)
values
    (1, 'ACME Ltd', 'ACTIVE'),
    (2, 'Widgets Ltd', 'ACTIVE'),
    (3, 'Suspended Ltd', 'SUSPENDED');

insert into account (id, customer_id, iban, currency, status)
values
    (1, 1, 'IE29AIBK93115212345678', 'EUR', 'ACTIVE'),
    (2, 1, 'IE64BOFI90001712345678', 'EUR', 'BLOCKED'),
    (3, 2, 'IE12BOFI90001712345679', 'EUR', 'ACTIVE');

insert into product_entitlement (id, customer_id, product_code, status)
values
    (1, 1, 'SEPA_BULK_CT', 'ACTIVE'),
    (2, 1, 'SEPA_INSTANT_CT', 'SUSPENDED'),
    (3, 2, 'SEPA_BULK_CT', 'ACTIVE');