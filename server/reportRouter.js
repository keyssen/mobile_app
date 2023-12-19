module.exports = (req, res, next) => {
  if (req.url.startsWith("/report") && req.method === "GET") {
    const { startDate, endDate } = req.query; // Assuming you pass startDate and endDate as query parameters
    delete require.cache[require.resolve("./data.json")];
    const data = require("./data.json");
    const filteredOrders = data.orders.filter((order) => {
      const orderDate = new Date(order.date);
      return orderDate >= new Date(startDate) && orderDate <= new Date(endDate);
    });
    const ordersWithProducts = data.order_with_products.filter((orderProd) => {
      return filteredOrders.some((order) => order.id === orderProd.orderId);
    });

    const revenueByProduct = {};
    const products = {};

    ordersWithProducts.forEach((orderProd) => {
      const productRevenue = orderProd.currentPrice * orderProd.count;
      if (revenueByProduct[orderProd.productId]) {
        revenueByProduct[orderProd.productId].sum += productRevenue;
        revenueByProduct[orderProd.productId].count += orderProd.count;
      } else {
        revenueByProduct[orderProd.productId] = {};
        revenueByProduct[orderProd.productId].sum = productRevenue;
        revenueByProduct[orderProd.productId].count = orderProd.count;
      }
    });
    data.products.forEach((product) => {
      products[product.id] = product;
    });

    const result = Object.keys(revenueByProduct).map((productId) => ({
      name: products[productId].name,
      currentPrice: products[productId].price,
      sum: revenueByProduct[productId].sum,
      count: revenueByProduct[productId].count,
    }));

    res.json(result);
  } else {
    next();
  }
};
