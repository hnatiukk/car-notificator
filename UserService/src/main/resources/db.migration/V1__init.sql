CREATE TABLE IF NOT EXISTS public.person
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    email character varying(64) COLLATE pg_catalog."default" NOT NULL,
    password character varying COLLATE pg_catalog."default" NOT NULL,
    tg_chat_id character varying COLLATE pg_catalog."default",
    role character varying(64) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT person_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.person
    OWNER to postgres;
CREATE TABLE IF NOT EXISTS public.subscription
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    brand character varying(64) COLLATE pg_catalog."default" NOT NULL,
    model character varying(64) COLLATE pg_catalog."default" NOT NULL,
    price_start integer,
    price_end integer,
    year_start integer,
    year_end integer,
    mileage_start integer,
    mileage_end integer,
    transmission integer,
    fuel_type integer,
    is_active boolean NOT NULL,
    request_url character varying COLLATE pg_catalog."default" NOT NULL,
    person_id bigint,
    CONSTRAINT subscription_pkey PRIMARY KEY (id),
    CONSTRAINT person_fkey FOREIGN KEY (person_id)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT DEFERRABLE
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.subscription
    OWNER to postgres;