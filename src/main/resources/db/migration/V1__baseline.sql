-- Baseline schema for Groomify, captured 2026-05-02 from Hibernate-generated
-- schema. This is the starting point for all future Flyway migrations.

--
-- Name: address; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.address (
    address_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    address_1 character varying(255) NOT NULL,
    address_2 character varying(255),
    address_3 character varying(255),
    city character varying(255) NOT NULL,
    country character varying(255) NOT NULL,
    postal_code integer NOT NULL,
    region character varying(255),
    state character varying(255) NOT NULL
);


--
-- Name: address_address_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.address_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: address_address_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.address_address_id_seq OWNED BY public.address.address_id;


--
-- Name: address_meta; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.address_meta (
    address_meta_id bigint NOT NULL,
    city character varying(255) NOT NULL,
    country character varying(255) DEFAULT 'India'::character varying NOT NULL,
    postal_code character varying(255),
    region character varying(255),
    state character varying(255) NOT NULL
);


--
-- Name: address_meta_address_meta_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.address_meta_address_meta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: address_meta_address_meta_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.address_meta_address_meta_id_seq OWNED BY public.address_meta.address_meta_id;


--
-- Name: chair_allocate; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.chair_allocate (
    chair_allocate_id bigint NOT NULL,
    is_available boolean DEFAULT true,
    floor character varying(10) DEFAULT 'GROUND'::character varying,
    num character varying(255) NOT NULL,
    is_occupied boolean DEFAULT false,
    customer bigint,
    CONSTRAINT chair_allocate_floor_check CHECK (((floor)::text = ANY ((ARRAY['BASEMENT4'::character varying, 'BASEMENT3'::character varying, 'BASEMENT2'::character varying, 'BASEMENT1'::character varying, 'GROUND'::character varying, 'FIRST'::character varying, 'SECOND'::character varying, 'THIRD'::character varying, 'FOURTH'::character varying, 'FIFTH'::character varying, 'SIXTH'::character varying, 'SEVENTH'::character varying, 'EIGHT'::character varying])::text[])))
);


--
-- Name: chair_allocate_chair_allocate_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.chair_allocate_chair_allocate_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: chair_allocate_chair_allocate_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.chair_allocate_chair_allocate_id_seq OWNED BY public.chair_allocate.chair_allocate_id;


--
-- Name: customer; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.customer (
    customer_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    account_closure_date date,
    alternate_contact bigint,
    contact bigint DEFAULT 0 NOT NULL,
    dob date,
    email character varying(40),
    first_name character varying(255) NOT NULL,
    gender character varying(10),
    govt_id character varying(255) NOT NULL,
    govt_id_snap_url character varying(255),
    last_name character varying(255),
    middle_name character varying(255),
    picture_url character varying(255),
    registration_id character varying(255) NOT NULL,
    username character varying(255) NOT NULL,
    billing_address bigint,
    govt_id_type bigint,
    password bigint,
    shipping_address bigint,
    CONSTRAINT customer_gender_check CHECK (((gender)::text = ANY ((ARRAY['MALE'::character varying, 'FEMALE'::character varying, 'OTHERS'::character varying])::text[])))
);


--
-- Name: customer_customer_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.customer_customer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: customer_customer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.customer_customer_id_seq OWNED BY public.customer.customer_id;


--
-- Name: customer_membership; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.customer_membership (
    membership_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    expiry_date date,
    issue_date date NOT NULL,
    membership_no character varying(255) NOT NULL,
    price_paid double precision NOT NULL,
    customer bigint,
    membership bigint
);


--
-- Name: customer_membership_membership_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.customer_membership_membership_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: customer_membership_membership_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.customer_membership_membership_id_seq OWNED BY public.customer_membership.membership_id;


--
-- Name: customer_order; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.customer_order (
    customer_order_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    appointment_date date,
    request_id character varying(255) NOT NULL,
    request_status smallint,
    customer bigint,
    CONSTRAINT customer_order_request_status_check CHECK (((request_status >= 0) AND (request_status <= 4)))
);


--
-- Name: customer_order_customer_order_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.customer_order_customer_order_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: customer_order_customer_order_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.customer_order_customer_order_id_seq OWNED BY public.customer_order.customer_order_id;


--
-- Name: customer_order_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.customer_order_history (
    coh_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    request_id character varying(255) NOT NULL,
    request_status smallint,
    customer bigint,
    CONSTRAINT customer_order_history_request_status_check CHECK (((request_status >= 0) AND (request_status <= 4)))
);


--
-- Name: customer_order_history_coh_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.customer_order_history_coh_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: customer_order_history_coh_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.customer_order_history_coh_id_seq OWNED BY public.customer_order_history.coh_id;


--
-- Name: customer_roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.customer_roles (
    customer_id bigint NOT NULL,
    role_id bigint NOT NULL
);


--
-- Name: employee; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.employee (
    employee_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    dob date,
    email character varying(40),
    first_name character varying(255) NOT NULL,
    gender character varying(10) DEFAULT 'MALE'::character varying,
    govt_id character varying(255) NOT NULL,
    govt_id_snap_url character varying(255),
    is_overtime_worker boolean DEFAULT false,
    joining_date timestamp(6) without time zone NOT NULL,
    leaving_date timestamp(6) without time zone,
    last_name character varying(255),
    middle_name character varying(255),
    picture_url character varying(255),
    primary_contact bigint DEFAULT 0 NOT NULL,
    rating character varying(2),
    registration_id character varying(255) NOT NULL,
    salary double precision,
    secondary_contact bigint,
    username character varying(255) NOT NULL,
    whatsapp_contact bigint,
    current_address bigint,
    govt_id_type bigint,
    password bigint,
    permanent_address bigint,
    qualification bigint,
    schedule bigint,
    CONSTRAINT employee_gender_check CHECK (((gender)::text = ANY ((ARRAY['MALE'::character varying, 'FEMALE'::character varying, 'OTHERS'::character varying])::text[]))),
    CONSTRAINT employee_rating_check CHECK (((rating)::text = ANY ((ARRAY['NOT_GOOD'::character varying, 'BELOW_AVERAGE'::character varying, 'AVERAGE'::character varying, 'EXCELLENT'::character varying, 'OUTSTANDING'::character varying])::text[])))
);


--
-- Name: employee_daily_activity; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.employee_daily_activity (
    schedule_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    activity character varying(255),
    "time" date NOT NULL,
    customer bigint,
    employee bigint,
    CONSTRAINT employee_daily_activity_activity_check CHECK (((activity)::text = ANY ((ARRAY['SALON_IN'::character varying, 'SALON_OUT'::character varying, 'LUNCH'::character varying, 'LUNCH_OVER'::character varying, 'ATTEND_CUSTOMER'::character varying, 'LEAVE_CUSTOMER'::character varying, 'VACATION'::character varying, 'MISCELLANEOUS'::character varying])::text[])))
);


--
-- Name: employee_daily_activity_schedule_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.employee_daily_activity_schedule_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: employee_daily_activity_schedule_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.employee_daily_activity_schedule_id_seq OWNED BY public.employee_daily_activity.schedule_id;


--
-- Name: employee_employee_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.employee_employee_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: employee_employee_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.employee_employee_id_seq OWNED BY public.employee.employee_id;


--
-- Name: employee_roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.employee_roles (
    employee_id bigint NOT NULL,
    role_id bigint NOT NULL
);


--
-- Name: employee_service; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.employee_service (
    employee_service_id bigint NOT NULL,
    employee bigint,
    service bigint
);


--
-- Name: employee_service_employee_service_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.employee_service_employee_service_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: employee_service_employee_service_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.employee_service_employee_service_id_seq OWNED BY public.employee_service.employee_service_id;


--
-- Name: govt_id_type; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.govt_id_type (
    type_id bigint NOT NULL,
    regex character varying(255),
    type_name character varying(255) NOT NULL
);


--
-- Name: govt_id_type_type_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.govt_id_type_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: govt_id_type_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.govt_id_type_type_id_seq OWNED BY public.govt_id_type.type_id;


--
-- Name: group_service; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.group_service (
    group_service_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    description character varying(255),
    end_date date NOT NULL,
    final_price double precision,
    marked_price double precision NOT NULL,
    name character varying(255) NOT NULL,
    start_date date NOT NULL
);


--
-- Name: group_service_group_service_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.group_service_group_service_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: group_service_group_service_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.group_service_group_service_id_seq OWNED BY public.group_service.group_service_id;


--
-- Name: group_service_package; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.group_service_package (
    description character varying(255),
    "GROUP_SERVICE_ID" bigint NOT NULL,
    group_service_id bigint NOT NULL,
    "SERVICE_ID" bigint NOT NULL,
    service_id bigint NOT NULL
);


--
-- Name: job_card; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.job_card (
    job_card_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    customer_feedback character varying(255),
    invoice_url character varying(255),
    job_end_time timestamp(6) without time zone,
    job_id character varying(255) NOT NULL,
    job_start_time timestamp(6) without time zone,
    job_status character varying(255) NOT NULL,
    paid_amount bigint,
    payment_amount bigint,
    payment_comments character varying(255),
    payment_mode character varying(255),
    customer_order bigint,
    CONSTRAINT job_card_job_status_check CHECK (((job_status)::text = ANY ((ARRAY['ENQUIRY'::character varying, 'PENDING'::character varying, 'INPROGRESS'::character varying, 'COMPLETED'::character varying, 'CANCELLED'::character varying])::text[]))),
    CONSTRAINT job_card_payment_mode_check CHECK (((payment_mode)::text = ANY ((ARRAY['CASH'::character varying, 'DEMAND_DRAFT'::character varying, 'PAYTM'::character varying, 'CREDIT_CARD'::character varying, 'PHONEPE'::character varying, 'AMAZON_PAY'::character varying, 'ACCOUNT_TRANSFER'::character varying])::text[])))
);


--
-- Name: job_card_details; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.job_card_details (
    cah_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    customer_feedback character varying(255),
    job_end_time timestamp(6) without time zone,
    job_start_time timestamp(6) without time zone,
    job_status character varying(255) NOT NULL,
    sub_job_id character varying(255) NOT NULL,
    employee bigint,
    job_card bigint,
    service bigint,
    CONSTRAINT job_card_details_job_status_check CHECK (((job_status)::text = ANY ((ARRAY['ENQUIRY'::character varying, 'PENDING'::character varying, 'INPROGRESS'::character varying, 'COMPLETED'::character varying, 'CANCELLED'::character varying])::text[])))
);


--
-- Name: job_card_details_cah_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.job_card_details_cah_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: job_card_details_cah_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.job_card_details_cah_id_seq OWNED BY public.job_card_details.cah_id;


--
-- Name: job_card_details_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.job_card_details_history (
    cah_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    customer_feedback character varying(255),
    job_end_time date,
    job_start_time date,
    job_status character varying(255) NOT NULL,
    sub_job_id character varying(255) NOT NULL,
    employee bigint,
    customer bigint,
    job_card_history bigint,
    service bigint,
    CONSTRAINT job_card_details_history_job_status_check CHECK (((job_status)::text = ANY ((ARRAY['ENQUIRY'::character varying, 'PENDING'::character varying, 'INPROGRESS'::character varying, 'COMPLETED'::character varying, 'CANCELLED'::character varying])::text[])))
);


--
-- Name: job_card_details_history_cah_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.job_card_details_history_cah_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: job_card_details_history_cah_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.job_card_details_history_cah_id_seq OWNED BY public.job_card_details_history.cah_id;


--
-- Name: job_card_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.job_card_history (
    job_card_history_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    customer_feedback character varying(255),
    invoice_url character varying(255),
    job_end_time date,
    job_id character varying(255) NOT NULL,
    job_start_time date,
    job_status character varying(255) NOT NULL,
    paid_amount bigint,
    payment_amount bigint,
    payment_comments character varying(255),
    payment_mode character varying(255) NOT NULL,
    customer_order_history bigint,
    CONSTRAINT job_card_history_job_status_check CHECK (((job_status)::text = ANY ((ARRAY['ENQUIRY'::character varying, 'PENDING'::character varying, 'INPROGRESS'::character varying, 'COMPLETED'::character varying, 'CANCELLED'::character varying])::text[]))),
    CONSTRAINT job_card_history_payment_mode_check CHECK (((payment_mode)::text = ANY ((ARRAY['CASH'::character varying, 'DEMAND_DRAFT'::character varying, 'PAYTM'::character varying, 'CREDIT_CARD'::character varying, 'PHONEPE'::character varying, 'AMAZON_PAY'::character varying, 'ACCOUNT_TRANSFER'::character varying])::text[])))
);


--
-- Name: job_card_history_job_card_history_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.job_card_history_job_card_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: job_card_history_job_card_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.job_card_history_job_card_history_id_seq OWNED BY public.job_card_history.job_card_history_id;


--
-- Name: job_card_job_card_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.job_card_job_card_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: job_card_job_card_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.job_card_job_card_id_seq OWNED BY public.job_card.job_card_id;


--
-- Name: membership_type; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.membership_type (
    type_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    description character varying(255),
    duration_days integer NOT NULL,
    last_date date,
    membership_code character varying(255),
    membership_type character varying(255) NOT NULL,
    price double precision NOT NULL,
    start_date date
);


--
-- Name: membership_type_type_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.membership_type_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: membership_type_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.membership_type_type_id_seq OWNED BY public.membership_type.type_id;


--
-- Name: metadata; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.metadata (
    metadata_id bigint NOT NULL,
    created_date timestamp(6) without time zone,
    description character varying(255),
    expiration_date timestamp(6) without time zone,
    meta_1 character varying(255),
    meta_2 character varying(255),
    meta_3 character varying(255),
    meta_4 character varying(255),
    meta_5 character varying(255),
    name character varying(255) NOT NULL
);


--
-- Name: metadata_metadata_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.metadata_metadata_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: metadata_metadata_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.metadata_metadata_id_seq OWNED BY public.metadata.metadata_id;


--
-- Name: password; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.password (
    password_id bigint NOT NULL,
    current_password character varying(255) NOT NULL,
    pwd_creation_date date NOT NULL,
    pwd_expiration_date date
);


--
-- Name: password_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.password_history (
    password_history_id bigint NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    hashed_password character varying(255) NOT NULL,
    password_id bigint NOT NULL
);


--
-- Name: password_history_password_history_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.password_history_password_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: password_history_password_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.password_history_password_history_id_seq OWNED BY public.password_history.password_history_id;


--
-- Name: password_password_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.password_password_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: password_password_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.password_password_id_seq OWNED BY public.password.password_id;


--
-- Name: product_category; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.product_category (
    product_category_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    category_name character varying(255) NOT NULL,
    description character varying(255),
    picture_url character varying(255)
);


--
-- Name: product_category_product_category_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.product_category_product_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: product_category_product_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.product_category_product_category_id_seq OWNED BY public.product_category.product_category_id;


--
-- Name: products; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.products (
    product_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    barcode character varying(255),
    cost_price real NOT NULL,
    full_price real NOT NULL,
    "hsn/sac" character varying(255),
    in_stock_quantity integer,
    name character varying(255) NOT NULL,
    product_image_url_1 character varying(255),
    product_image_url_2 character varying(255),
    product_image_url_3 character varying(255),
    product_image_url_4 character varying(255),
    product_image_url_5 character varying(255),
    product_short_desc character varying(255),
    product_thumb_url character varying(255),
    quantity_alert integer,
    special_price real,
    weight character varying(255) NOT NULL,
    product_category bigint
);


--
-- Name: products_product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.products_product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: products_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.products_product_id_seq OWNED BY public.products.product_id;


--
-- Name: qualification; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.qualification (
    qualification_id bigint NOT NULL,
    qualification_type character varying(255),
    CONSTRAINT qualification_qualification_type_check CHECK (((qualification_type)::text = ANY ((ARRAY['BELOW_10'::character varying, 'INTERMEDIATE'::character varying, 'HIGH_SCHOOL'::character varying, 'BACHELORS'::character varying, 'MASTERS'::character varying, 'PHD'::character varying])::text[])))
);


--
-- Name: qualification_qualification_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.qualification_qualification_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: qualification_qualification_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.qualification_qualification_id_seq OWNED BY public.qualification.qualification_id;


--
-- Name: roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.roles (
    role_id bigint NOT NULL,
    role_name character varying(60),
    CONSTRAINT roles_role_name_check CHECK (((role_name)::text = ANY ((ARRAY['ROLE_EMPLOYEE'::character varying, 'ROLE_ADMIN'::character varying, 'ROLE_CUSTOMER'::character varying, 'ROLE_FRONT_DESK'::character varying, 'ROLE_SUPERVISOR'::character varying])::text[])))
);


--
-- Name: roles_role_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.roles_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: roles_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.roles_role_id_seq OWNED BY public.roles.role_id;


--
-- Name: schedule; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.schedule (
    schedule_id bigint NOT NULL,
    in_time time(6) without time zone NOT NULL,
    is_shift_active boolean DEFAULT true,
    out_time time(6) without time zone NOT NULL,
    week_end_day character varying(255) DEFAULT 'SUNDAY'::character varying,
    week_start_day character varying(255) DEFAULT 'MONDAY'::character varying,
    CONSTRAINT schedule_week_end_day_check CHECK (((week_end_day)::text = ANY ((ARRAY['MONDAY'::character varying, 'TUESDAY'::character varying, 'WEDNESDAY'::character varying, 'THURSDAY'::character varying, 'FRIDAY'::character varying, 'SATURDAY'::character varying, 'SUNDAY'::character varying])::text[]))),
    CONSTRAINT schedule_week_start_day_check CHECK (((week_start_day)::text = ANY ((ARRAY['MONDAY'::character varying, 'TUESDAY'::character varying, 'WEDNESDAY'::character varying, 'THURSDAY'::character varying, 'FRIDAY'::character varying, 'SATURDAY'::character varying, 'SUNDAY'::character varying])::text[])))
);


--
-- Name: schedule_schedule_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.schedule_schedule_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: schedule_schedule_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.schedule_schedule_id_seq OWNED BY public.schedule.schedule_id;


--
-- Name: service; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.service (
    service_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    "hsn/sac" character varying(255),
    is_active boolean DEFAULT true,
    name character varying(255) NOT NULL,
    price double precision,
    service_time time(6) without time zone,
    service_type bigint
);


--
-- Name: service_service_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.service_service_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: service_service_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.service_service_id_seq OWNED BY public.service.service_id;


--
-- Name: service_type; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.service_type (
    service_type_id bigint NOT NULL,
    name character varying(255) NOT NULL
);


--
-- Name: service_type_service_type_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.service_type_service_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: service_type_service_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.service_type_service_type_id_seq OWNED BY public.service_type.service_type_id;


--
-- Name: stock_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.stock_history (
    stock_history_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    notes character varying(255),
    price_sold_per_unit real,
    quantity_change integer,
    stock_action character varying(255) NOT NULL,
    stock_quantity integer,
    customer bigint,
    product bigint,
    CONSTRAINT stock_history_stock_action_check CHECK (((stock_action)::text = ANY ((ARRAY['SOLD'::character varying, 'ADD'::character varying, 'SELF_CONSUME'::character varying, 'INVOICED'::character varying, 'ADJUSTMENT'::character varying, 'DAMAGED'::character varying])::text[])))
);


--
-- Name: stock_history_stock_history_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.stock_history_stock_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: stock_history_stock_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.stock_history_stock_history_id_seq OWNED BY public.stock_history.stock_history_id;


--
-- Name: vendor; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.vendor (
    vendor_id bigint NOT NULL,
    created_by character varying(255) DEFAULT 'admin'::character varying,
    creation_date timestamp(6) without time zone NOT NULL,
    last_modified_by character varying(255) DEFAULT 'admin'::character varying,
    last_modification_date timestamp(6) without time zone NOT NULL,
    company_name character varying(255) NOT NULL,
    contact_first_name character varying(255) NOT NULL,
    gender character varying(10) DEFAULT 'MALE'::character varying,
    contact_last_name character varying(255),
    logo_url character varying(255),
    payment_method character varying(255) DEFAULT 'ACCOUNT_TRANSFER'::character varying,
    rating character varying(255) DEFAULT 'AVERAGE'::character varying NOT NULL,
    site_url character varying(255),
    vendor_reg_id character varying(255) NOT NULL,
    address bigint,
    product bigint,
    warehouse_address bigint,
    CONSTRAINT vendor_gender_check CHECK (((gender)::text = ANY ((ARRAY['MALE'::character varying, 'FEMALE'::character varying, 'OTHERS'::character varying])::text[]))),
    CONSTRAINT vendor_payment_method_check CHECK (((payment_method)::text = ANY ((ARRAY['CASH'::character varying, 'DEMAND_DRAFT'::character varying, 'PAYTM'::character varying, 'CREDIT_CARD'::character varying, 'PHONEPE'::character varying, 'AMAZON_PAY'::character varying, 'ACCOUNT_TRANSFER'::character varying])::text[]))),
    CONSTRAINT vendor_rating_check CHECK (((rating)::text = ANY ((ARRAY['NOT_GOOD'::character varying, 'BELOW_AVERAGE'::character varying, 'AVERAGE'::character varying, 'EXCELLENT'::character varying, 'OUTSTANDING'::character varying])::text[])))
);


--
-- Name: vendor_vendor_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.vendor_vendor_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: vendor_vendor_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.vendor_vendor_id_seq OWNED BY public.vendor.vendor_id;


--
-- Name: address address_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.address ALTER COLUMN address_id SET DEFAULT nextval('public.address_address_id_seq'::regclass);


--
-- Name: address_meta address_meta_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.address_meta ALTER COLUMN address_meta_id SET DEFAULT nextval('public.address_meta_address_meta_id_seq'::regclass);


--
-- Name: chair_allocate chair_allocate_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chair_allocate ALTER COLUMN chair_allocate_id SET DEFAULT nextval('public.chair_allocate_chair_allocate_id_seq'::regclass);


--
-- Name: customer customer_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer ALTER COLUMN customer_id SET DEFAULT nextval('public.customer_customer_id_seq'::regclass);


--
-- Name: customer_membership membership_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_membership ALTER COLUMN membership_id SET DEFAULT nextval('public.customer_membership_membership_id_seq'::regclass);


--
-- Name: customer_order customer_order_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_order ALTER COLUMN customer_order_id SET DEFAULT nextval('public.customer_order_customer_order_id_seq'::regclass);


--
-- Name: customer_order_history coh_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_order_history ALTER COLUMN coh_id SET DEFAULT nextval('public.customer_order_history_coh_id_seq'::regclass);


--
-- Name: employee employee_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee ALTER COLUMN employee_id SET DEFAULT nextval('public.employee_employee_id_seq'::regclass);


--
-- Name: employee_daily_activity schedule_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee_daily_activity ALTER COLUMN schedule_id SET DEFAULT nextval('public.employee_daily_activity_schedule_id_seq'::regclass);


--
-- Name: employee_service employee_service_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee_service ALTER COLUMN employee_service_id SET DEFAULT nextval('public.employee_service_employee_service_id_seq'::regclass);


--
-- Name: govt_id_type type_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.govt_id_type ALTER COLUMN type_id SET DEFAULT nextval('public.govt_id_type_type_id_seq'::regclass);


--
-- Name: group_service group_service_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.group_service ALTER COLUMN group_service_id SET DEFAULT nextval('public.group_service_group_service_id_seq'::regclass);


--
-- Name: job_card job_card_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card ALTER COLUMN job_card_id SET DEFAULT nextval('public.job_card_job_card_id_seq'::regclass);


--
-- Name: job_card_details cah_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card_details ALTER COLUMN cah_id SET DEFAULT nextval('public.job_card_details_cah_id_seq'::regclass);


--
-- Name: job_card_details_history cah_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card_details_history ALTER COLUMN cah_id SET DEFAULT nextval('public.job_card_details_history_cah_id_seq'::regclass);


--
-- Name: job_card_history job_card_history_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card_history ALTER COLUMN job_card_history_id SET DEFAULT nextval('public.job_card_history_job_card_history_id_seq'::regclass);


--
-- Name: membership_type type_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.membership_type ALTER COLUMN type_id SET DEFAULT nextval('public.membership_type_type_id_seq'::regclass);


--
-- Name: metadata metadata_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.metadata ALTER COLUMN metadata_id SET DEFAULT nextval('public.metadata_metadata_id_seq'::regclass);


--
-- Name: password password_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.password ALTER COLUMN password_id SET DEFAULT nextval('public.password_password_id_seq'::regclass);


--
-- Name: password_history password_history_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.password_history ALTER COLUMN password_history_id SET DEFAULT nextval('public.password_history_password_history_id_seq'::regclass);


--
-- Name: product_category product_category_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_category ALTER COLUMN product_category_id SET DEFAULT nextval('public.product_category_product_category_id_seq'::regclass);


--
-- Name: products product_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products ALTER COLUMN product_id SET DEFAULT nextval('public.products_product_id_seq'::regclass);


--
-- Name: qualification qualification_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.qualification ALTER COLUMN qualification_id SET DEFAULT nextval('public.qualification_qualification_id_seq'::regclass);


--
-- Name: roles role_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles ALTER COLUMN role_id SET DEFAULT nextval('public.roles_role_id_seq'::regclass);


--
-- Name: schedule schedule_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedule ALTER COLUMN schedule_id SET DEFAULT nextval('public.schedule_schedule_id_seq'::regclass);


--
-- Name: service service_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.service ALTER COLUMN service_id SET DEFAULT nextval('public.service_service_id_seq'::regclass);


--
-- Name: service_type service_type_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.service_type ALTER COLUMN service_type_id SET DEFAULT nextval('public.service_type_service_type_id_seq'::regclass);


--
-- Name: stock_history stock_history_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_history ALTER COLUMN stock_history_id SET DEFAULT nextval('public.stock_history_stock_history_id_seq'::regclass);


--
-- Name: vendor vendor_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.vendor ALTER COLUMN vendor_id SET DEFAULT nextval('public.vendor_vendor_id_seq'::regclass);


--
-- Name: customer_membership UK1252ppce0o8jrmmpvt65efwqu; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_membership
    ADD CONSTRAINT "UK1252ppce0o8jrmmpvt65efwqu" UNIQUE (membership_no);


--
-- Name: customer UK35ori016o04ma504m0ciimyjq; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT "UK35ori016o04ma504m0ciimyjq" UNIQUE (username);


--
-- Name: service UK5sm1g8faj3ky2te5klfcpl0h5; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.service
    ADD CONSTRAINT "UK5sm1g8faj3ky2te5klfcpl0h5" UNIQUE (name, "hsn/sac", service_type);


--
-- Name: customer UK6e0510yk7t1xie02x8st5u7da; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT "UK6e0510yk7t1xie02x8st5u7da" UNIQUE (email);


--
-- Name: employee UKnfh9ltoe6qcmirv4b3pwqrfem; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT "UKnfh9ltoe6qcmirv4b3pwqrfem" UNIQUE (username);


--
-- Name: address_meta address_meta_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.address_meta
    ADD CONSTRAINT address_meta_pkey PRIMARY KEY (address_meta_id);


--
-- Name: address address_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.address
    ADD CONSTRAINT address_pkey PRIMARY KEY (address_id);


--
-- Name: chair_allocate chair_allocate_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chair_allocate
    ADD CONSTRAINT chair_allocate_pkey PRIMARY KEY (chair_allocate_id);


--
-- Name: customer_membership customer_membership_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_membership
    ADD CONSTRAINT customer_membership_pkey PRIMARY KEY (membership_id);


--
-- Name: customer_order_history customer_order_history_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_order_history
    ADD CONSTRAINT customer_order_history_pkey PRIMARY KEY (coh_id);


--
-- Name: customer_order customer_order_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_order
    ADD CONSTRAINT customer_order_pkey PRIMARY KEY (customer_order_id);


--
-- Name: customer customer_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (customer_id);


--
-- Name: customer_roles customer_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_roles
    ADD CONSTRAINT customer_roles_pkey PRIMARY KEY (customer_id, role_id);


--
-- Name: employee_daily_activity employee_daily_activity_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee_daily_activity
    ADD CONSTRAINT employee_daily_activity_pkey PRIMARY KEY (schedule_id);


--
-- Name: employee employee_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_pkey PRIMARY KEY (employee_id);


--
-- Name: employee_roles employee_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee_roles
    ADD CONSTRAINT employee_roles_pkey PRIMARY KEY (employee_id, role_id);


--
-- Name: employee_service employee_service_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee_service
    ADD CONSTRAINT employee_service_pkey PRIMARY KEY (employee_service_id);


--
-- Name: govt_id_type govt_id_type_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.govt_id_type
    ADD CONSTRAINT govt_id_type_pkey PRIMARY KEY (type_id);


--
-- Name: group_service_package group_service_package_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.group_service_package
    ADD CONSTRAINT group_service_package_pkey PRIMARY KEY ("GROUP_SERVICE_ID", "SERVICE_ID");


--
-- Name: group_service group_service_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.group_service
    ADD CONSTRAINT group_service_pkey PRIMARY KEY (group_service_id);


--
-- Name: job_card_details_history job_card_details_history_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card_details_history
    ADD CONSTRAINT job_card_details_history_pkey PRIMARY KEY (cah_id);


--
-- Name: job_card_details job_card_details_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card_details
    ADD CONSTRAINT job_card_details_pkey PRIMARY KEY (cah_id);


--
-- Name: job_card_history job_card_history_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card_history
    ADD CONSTRAINT job_card_history_pkey PRIMARY KEY (job_card_history_id);


--
-- Name: job_card job_card_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card
    ADD CONSTRAINT job_card_pkey PRIMARY KEY (job_card_id);


--
-- Name: membership_type membership_type_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.membership_type
    ADD CONSTRAINT membership_type_pkey PRIMARY KEY (type_id);


--
-- Name: metadata metadata_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.metadata
    ADD CONSTRAINT metadata_pkey PRIMARY KEY (metadata_id);


--
-- Name: password_history password_history_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.password_history
    ADD CONSTRAINT password_history_pkey PRIMARY KEY (password_history_id);


--
-- Name: password password_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.password
    ADD CONSTRAINT password_pkey PRIMARY KEY (password_id);


--
-- Name: product_category product_category_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_category
    ADD CONSTRAINT product_category_pkey PRIMARY KEY (product_category_id);


--
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (product_id);


--
-- Name: qualification qualification_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.qualification
    ADD CONSTRAINT qualification_pkey PRIMARY KEY (qualification_id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (role_id);


--
-- Name: schedule schedule_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedule
    ADD CONSTRAINT schedule_pkey PRIMARY KEY (schedule_id);


--
-- Name: service service_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.service
    ADD CONSTRAINT service_pkey PRIMARY KEY (service_id);


--
-- Name: service_type service_type_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.service_type
    ADD CONSTRAINT service_type_pkey PRIMARY KEY (service_type_id);


--
-- Name: stock_history stock_history_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_history
    ADD CONSTRAINT stock_history_pkey PRIMARY KEY (stock_history_id);


--
-- Name: customer uk_2eknrlbrverbuqn974kmisy8s; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT uk_2eknrlbrverbuqn974kmisy8s UNIQUE (registration_id);


--
-- Name: employee uk_3gii3g00sdbnckmaqjglvh091; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT uk_3gii3g00sdbnckmaqjglvh091 UNIQUE (govt_id_snap_url);


--
-- Name: customer uk_5f63uhpt1ql19mdkyks5boser; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT uk_5f63uhpt1ql19mdkyks5boser UNIQUE (govt_id);


--
-- Name: service_type uk_5xqxi6a47dld2eppt6hl3jl50; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.service_type
    ADD CONSTRAINT uk_5xqxi6a47dld2eppt6hl3jl50 UNIQUE (name);


--
-- Name: customer uk_79gjqa1ivdihdqvv2x7dv36nw; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT uk_79gjqa1ivdihdqvv2x7dv36nw UNIQUE (password);


--
-- Name: employee uk_7u0lbcpn8njh0q3h74ii9qaq3; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT uk_7u0lbcpn8njh0q3h74ii9qaq3 UNIQUE (password);


--
-- Name: customer uk_8m9l55m0vin0igughoo310l59; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT uk_8m9l55m0vin0igughoo310l59 UNIQUE (picture_url);


--
-- Name: chair_allocate uk_8roswysluby8vpkpomxrl83c2; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chair_allocate
    ADD CONSTRAINT uk_8roswysluby8vpkpomxrl83c2 UNIQUE (num);


--
-- Name: customer_order uk_bxffyncspseerxqmraegpjgo5; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_order
    ADD CONSTRAINT uk_bxffyncspseerxqmraegpjgo5 UNIQUE (request_id);


--
-- Name: vendor uk_cylsp5btbykfc5onpoemy53ho; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.vendor
    ADD CONSTRAINT uk_cylsp5btbykfc5onpoemy53ho UNIQUE (vendor_reg_id);


--
-- Name: employee uk_d3g38ghaeo06ipijddrfgkeds; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT uk_d3g38ghaeo06ipijddrfgkeds UNIQUE (registration_id);


--
-- Name: customer uk_ec0wg8dsuvxvnxb1tj0m1ekjn; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT uk_ec0wg8dsuvxvnxb1tj0m1ekjn UNIQUE (govt_id_snap_url);


--
-- Name: chair_allocate uk_k0hp6d1s78k7irepiy6lp53ef; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chair_allocate
    ADD CONSTRAINT uk_k0hp6d1s78k7irepiy6lp53ef UNIQUE (customer);


--
-- Name: metadata uk_m4sxoe5dtju16d0ojh0dy9d6; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.metadata
    ADD CONSTRAINT uk_m4sxoe5dtju16d0ojh0dy9d6 UNIQUE (name);


--
-- Name: roles uk_nb4h0p6txrmfc0xbrd1kglp9t; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT uk_nb4h0p6txrmfc0xbrd1kglp9t UNIQUE (role_name);


--
-- Name: employee uk_ola4eywsdhmfxt119v57aho6a; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT uk_ola4eywsdhmfxt119v57aho6a UNIQUE (picture_url);


--
-- Name: employee uk_p2oh3c7dqr81f2hd70utuo2pv; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT uk_p2oh3c7dqr81f2hd70utuo2pv UNIQUE (govt_id);


--
-- Name: customer_order_history uk_pfyajp6j6vw1p6wsi9qwsx5ll; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_order_history
    ADD CONSTRAINT uk_pfyajp6j6vw1p6wsi9qwsx5ll UNIQUE (request_id);


--
-- Name: employee uk_q3nme3uq00618x7nhnvtm21yj; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT uk_q3nme3uq00618x7nhnvtm21yj UNIQUE (email);


--
-- Name: products uk_qfr8vf85k3q1xinifvsl1eynf; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT uk_qfr8vf85k3q1xinifvsl1eynf UNIQUE (barcode);


--
-- Name: job_card_details uk_ro1eqavqpbn1csu3xcy5pawp3; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card_details
    ADD CONSTRAINT uk_ro1eqavqpbn1csu3xcy5pawp3 UNIQUE (sub_job_id);


--
-- Name: vendor vendor_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.vendor
    ADD CONSTRAINT vendor_pkey PRIMARY KEY (vendor_id);


--
-- Name: idx_password_history_password_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_password_history_password_id ON public.password_history USING btree (password_id);


--
-- Name: vendor FK2f5557r44llyp5jtn5x4y7t2t; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.vendor
    ADD CONSTRAINT "FK2f5557r44llyp5jtn5x4y7t2t" FOREIGN KEY (address) REFERENCES public.address(address_id);


--
-- Name: vendor FK2p7lr0d0244npnws98civtk0y; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.vendor
    ADD CONSTRAINT "FK2p7lr0d0244npnws98civtk0y" FOREIGN KEY (product) REFERENCES public.products(product_id);


--
-- Name: employee_daily_activity FK35hq96oe202if39aoll40fg; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee_daily_activity
    ADD CONSTRAINT "FK35hq96oe202if39aoll40fg" FOREIGN KEY (employee) REFERENCES public.employee(employee_id);


--
-- Name: job_card_details_history FK39tq6pmerru6qt9xfptm5scmu; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card_details_history
    ADD CONSTRAINT "FK39tq6pmerru6qt9xfptm5scmu" FOREIGN KEY (service) REFERENCES public.service(service_id);


--
-- Name: employee FK3pe70n08e5vkn0owq8140ocyc; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT "FK3pe70n08e5vkn0owq8140ocyc" FOREIGN KEY (current_address) REFERENCES public.address(address_id);


--
-- Name: customer_order_history FK4656islxri5ryvr3uqgkreiq3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_order_history
    ADD CONSTRAINT "FK4656islxri5ryvr3uqgkreiq3" FOREIGN KEY (customer) REFERENCES public.customer(customer_id);


--
-- Name: job_card_details FK4mdftxv4fi9w1qth7hq7niqb6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card_details
    ADD CONSTRAINT "FK4mdftxv4fi9w1qth7hq7niqb6" FOREIGN KEY (employee) REFERENCES public.employee(employee_id);


--
-- Name: customer FK639u8v0pegqrqu5ktxkb2g3t0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT "FK639u8v0pegqrqu5ktxkb2g3t0" FOREIGN KEY (password) REFERENCES public.password(password_id);


--
-- Name: job_card_details_history FK68lcrm5qlni9je9hwgsfw87hw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card_details_history
    ADD CONSTRAINT "FK68lcrm5qlni9je9hwgsfw87hw" FOREIGN KEY (employee) REFERENCES public.employee(employee_id);


--
-- Name: customer FK69kxukb1tdui9btkevi28yuwc; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT "FK69kxukb1tdui9btkevi28yuwc" FOREIGN KEY (billing_address) REFERENCES public.address(address_id);


--
-- Name: employee FK6o4g6j7utu2xvxjlm1fq9vffj; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT "FK6o4g6j7utu2xvxjlm1fq9vffj" FOREIGN KEY (qualification) REFERENCES public.qualification(qualification_id);


--
-- Name: stock_history FK7jdc668jxq5n4jfodboidbls5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_history
    ADD CONSTRAINT "FK7jdc668jxq5n4jfodboidbls5" FOREIGN KEY (product) REFERENCES public.products(product_id);


--
-- Name: customer_membership FK7ohg2743luupa8yg2mlaqr1dw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_membership
    ADD CONSTRAINT "FK7ohg2743luupa8yg2mlaqr1dw" FOREIGN KEY (membership) REFERENCES public.membership_type(type_id);


--
-- Name: customer FK7ulnp0o6bki5u3ibdvct6l3c7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT "FK7ulnp0o6bki5u3ibdvct6l3c7" FOREIGN KEY (govt_id_type) REFERENCES public.govt_id_type(type_id);


--
-- Name: employee FK9rvcdg7lu4776cbu87jmi5i4u; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT "FK9rvcdg7lu4776cbu87jmi5i4u" FOREIGN KEY (password) REFERENCES public.password(password_id);


--
-- Name: job_card FKa0oxedn258iv3663sj9pq66fi; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card
    ADD CONSTRAINT "FKa0oxedn258iv3663sj9pq66fi" FOREIGN KEY (customer_order) REFERENCES public.customer_order(customer_order_id);


--
-- Name: customer_roles FKb2xouo8p1nopmvssb2u57e0ci; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_roles
    ADD CONSTRAINT "FKb2xouo8p1nopmvssb2u57e0ci" FOREIGN KEY (customer_id) REFERENCES public.customer(customer_id);


--
-- Name: group_service_package FKbfd9tcdsy4ilcllvddfl0q5qv; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.group_service_package
    ADD CONSTRAINT "FKbfd9tcdsy4ilcllvddfl0q5qv" FOREIGN KEY (group_service_id) REFERENCES public.group_service(group_service_id);


--
-- Name: job_card_details FKbslka0pvc85qmasd622pgdsnx; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card_details
    ADD CONSTRAINT "FKbslka0pvc85qmasd622pgdsnx" FOREIGN KEY (service) REFERENCES public.service(service_id);


--
-- Name: employee_service FKc0cr5b0u01ylk90c8e3rqs57g; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee_service
    ADD CONSTRAINT "FKc0cr5b0u01ylk90c8e3rqs57g" FOREIGN KEY (service) REFERENCES public.service(service_id);


--
-- Name: stock_history FKd1soikaqq1ap1l1elwj3dilnv; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_history
    ADD CONSTRAINT "FKd1soikaqq1ap1l1elwj3dilnv" FOREIGN KEY (customer) REFERENCES public.customer(customer_id);


--
-- Name: employee_roles FKdacr31ox4kev4i3vvwoj4e6r7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee_roles
    ADD CONSTRAINT "FKdacr31ox4kev4i3vvwoj4e6r7" FOREIGN KEY (employee_id) REFERENCES public.employee(employee_id);


--
-- Name: employee FKee95arq8080ssb2qijl5drrxa; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT "FKee95arq8080ssb2qijl5drrxa" FOREIGN KEY (govt_id_type) REFERENCES public.govt_id_type(type_id);


--
-- Name: job_card_details FKelse0u8eyv2xi9394fh8bsu21; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card_details
    ADD CONSTRAINT "FKelse0u8eyv2xi9394fh8bsu21" FOREIGN KEY (job_card) REFERENCES public.job_card(job_card_id);


--
-- Name: chair_allocate FKerplklp5gmh9rafc15agx9k3q; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chair_allocate
    ADD CONSTRAINT "FKerplklp5gmh9rafc15agx9k3q" FOREIGN KEY (customer) REFERENCES public.customer(customer_id);


--
-- Name: employee_service FKf230g6t91uy22f3sdaaoymss2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee_service
    ADD CONSTRAINT "FKf230g6t91uy22f3sdaaoymss2" FOREIGN KEY (employee) REFERENCES public.employee(employee_id);


--
-- Name: employee_daily_activity FKgu7yvwe8b5p7pqej1gvnka1hj; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee_daily_activity
    ADD CONSTRAINT "FKgu7yvwe8b5p7pqej1gvnka1hj" FOREIGN KEY (customer) REFERENCES public.customer(customer_id);


--
-- Name: customer_membership FKhg9k24j7g80lnrb19fvigf96f; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_membership
    ADD CONSTRAINT "FKhg9k24j7g80lnrb19fvigf96f" FOREIGN KEY (customer) REFERENCES public.customer(customer_id);


--
-- Name: employee FKihu6qmorj19mmhyaqrb9f9q5p; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT "FKihu6qmorj19mmhyaqrb9f9q5p" FOREIGN KEY (permanent_address) REFERENCES public.address(address_id);


--
-- Name: job_card_details_history FKl51eha0qraaagth7mt3mlrye; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card_details_history
    ADD CONSTRAINT "FKl51eha0qraaagth7mt3mlrye" FOREIGN KEY (job_card_history) REFERENCES public.job_card_history(job_card_history_id);


--
-- Name: service FKlg9i4pirrlvkx126d6g3kechb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.service
    ADD CONSTRAINT "FKlg9i4pirrlvkx126d6g3kechb" FOREIGN KEY (service_type) REFERENCES public.service_type(service_type_id);


--
-- Name: employee FKlu2qglb6sx7ilggbxev40yt5j; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT "FKlu2qglb6sx7ilggbxev40yt5j" FOREIGN KEY (schedule) REFERENCES public.schedule(schedule_id);


--
-- Name: customer FKmbhmbw8ardyycjk3xjwkbl25k; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT "FKmbhmbw8ardyycjk3xjwkbl25k" FOREIGN KEY (shipping_address) REFERENCES public.address(address_id);


--
-- Name: password_history FKmexyaoiedlmtkgd73mou4gwgn; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.password_history
    ADD CONSTRAINT "FKmexyaoiedlmtkgd73mou4gwgn" FOREIGN KEY (password_id) REFERENCES public.password(password_id);


--
-- Name: job_card_history FKn06rx5pxokey9drl0552r96l0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card_history
    ADD CONSTRAINT "FKn06rx5pxokey9drl0552r96l0" FOREIGN KEY (customer_order_history) REFERENCES public.customer_order_history(coh_id);


--
-- Name: products FKnuau76nn03yin9jo8i9itahfn; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT "FKnuau76nn03yin9jo8i9itahfn" FOREIGN KEY (product_category) REFERENCES public.product_category(product_category_id);


--
-- Name: group_service_package FKp29y0qvkipash8qcot7ll28od; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.group_service_package
    ADD CONSTRAINT "FKp29y0qvkipash8qcot7ll28od" FOREIGN KEY (service_id) REFERENCES public.service(service_id);


--
-- Name: customer_roles FKq2w1d1mac9f9j86y6rjmjw6bg; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_roles
    ADD CONSTRAINT "FKq2w1d1mac9f9j86y6rjmjw6bg" FOREIGN KEY (role_id) REFERENCES public.roles(role_id);


--
-- Name: vendor FKt16uanf60oclswfk04ok3087w; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.vendor
    ADD CONSTRAINT "FKt16uanf60oclswfk04ok3087w" FOREIGN KEY (warehouse_address) REFERENCES public.address(address_id);


--
-- Name: customer_order FKt6oc62m8p8o6ca3m7v24idh37; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_order
    ADD CONSTRAINT "FKt6oc62m8p8o6ca3m7v24idh37" FOREIGN KEY (customer) REFERENCES public.customer(customer_id);


--
-- Name: job_card_details_history FKt9gk8p8m7x8wv29bjpj0gsqdd; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_card_details_history
    ADD CONSTRAINT "FKt9gk8p8m7x8wv29bjpj0gsqdd" FOREIGN KEY (customer) REFERENCES public.customer(customer_id);


--
-- Name: employee_roles FKw3pfqsijn1wy0wdlb7hvmhal; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.employee_roles
    ADD CONSTRAINT "FKw3pfqsijn1wy0wdlb7hvmhal" FOREIGN KEY (role_id) REFERENCES public.roles(role_id);


