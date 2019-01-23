#Question 1 from Details database

Drop function if exists average_age_per_position;

DELIMITER //

CREATE function average_age_per_position

(
	position_param  varchar(25)
)

RETURNS DECIMAL(9,2)

BEGIN

DECLARE avg_var DECIMAL(9,2);

	SELECT round(avg(age))
    INTO avg_var
    FROM details
    WHERE position = position_param;
    
RETURN avg_var;

END//

select average_age_per_position ("Lecturer");




#Questions from om database

# 1. List all orders that took more than 10 days to ship 

	SELECT * FROM orders WHERE datediff(shipped_date, order_date)>10;
    
    
# 2. What is the full name of the customer who placed order_id = 70
	
    SELECT concat(customer_first_name , ' ' , customer_last_name) as customer 
    from orders 
    inner join customers on
    orders.customer_id = customers.customer_id 
    where order_id = 70;
	
    
# 3. List the title of all the items that were ordered on order id = 264

	SELECT title from items
    inner join order_details
	on items.item_id = order_details.item_id
    where order_id = 264;
    
    
# 4. Write a stored function named order_days that returns the number of days it took to ship an order (i.e. difference between shipped_date and order_date)

	Drop function if exists order_days;
    
    DELIMITER //
    
	CREATE FUNCTION order_days
    (
		order_id_param   INT
    )
    RETURNS INT
    
    BEGIN
    
    DECLARE days_var INT;
    
		SELECT datediff(shipped_date, order_date)
		INTO days_var
		FROM orders
		WHERE  order_id = order_id_param;
    
	RETURN days_var;

	END//

select order_days(19);
