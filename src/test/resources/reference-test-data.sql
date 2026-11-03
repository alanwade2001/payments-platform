delete from submission;
delete from product_entitlement;
delete from account;
delete from customer;

-- Happy path
insert into customer (id, name, status)
values (1, 'ACME Ltd', 'ACTIVE');

-- Suspended customer
insert into customer (id, name, status)
values (2, 'Suspended Customer Ltd', 'SUSPENDED');

-- Blocked account customer
insert into customer (id, name, status)
values (3, 'Blocked Account Ltd', 'ACTIVE');

-- Suspended Bulk CT product customer
insert into customer (id, name, status)
values (4, 'Suspended Product Ltd', 'ACTIVE');


insert into account (id, customer_id, iban, currency, status)
values
    (1, 1, 'IE29AIBK93115212345678', 'EUR', 'ACTIVE'),
    (2, 3, 'IE64BOFI90001712345678', 'EUR', 'BLOCKED'),
    (3, 4, 'IE12BOFI90001712345679', 'EUR', 'ACTIVE');


insert into product_entitlement (
    id,
    customer_id,
    product_code,
    status
)
values
    (1, 1, 'SEPA_BULK_CT', 'ACTIVE'),
    (2, 3, 'SEPA_BULK_CT', 'ACTIVE'),
    (3, 4, 'SEPA_BULK_CT', 'SUSPENDED');