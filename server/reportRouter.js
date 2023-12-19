
module.exports = (req, res, next) => {
  if (req.url.startsWith('/report') && req.method === 'GET') {
    const { startDate, endDate } = req.query; // Assuming you pass startDate and endDate as query parameters
    delete require.cache[require.resolve('./data.json')];
    const data = require('./data.json');
    const filteredOrders = data.orders.filter(order => {
      const orderDate = new Date(order.date);
      return orderDate >= new Date(startDate) && orderDate <= new Date(endDate);
    });
    const ordersWithProducts = data.order_with_products.filter(orderProd => {
      return filteredOrders.some(order => order.id === orderProd.orderId);
    });

    const revenueByProduct = {};
    const products = {};

    ordersWithProducts.forEach(orderProd => {
      const productRevenue = orderProd.currentPrice * orderProd.count;

      if (revenueByProduct[orderProd.productId]) {
        revenueByProduct[orderProd.productId] += productRevenue;
      } else {
      products[orderProd.productId] = data.products.find(element=>
        element.id === orderProd.productId
      ).name
        revenueByProduct[orderProd.productId] = productRevenue;
      }
    });

    const result = Object.keys(revenueByProduct).map(productId => ({
      name: products[productId],
      sum: revenueByProduct[productId],
    }));

    res.json(result);
  } else {
    next();
  }
};