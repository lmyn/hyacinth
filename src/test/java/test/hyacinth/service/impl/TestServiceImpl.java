package test.hyacinth.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import test.hyacinth.model.Trade;
import test.hyacinth.service.TestService;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/3/24
 * Time: 20:04
 */
@Service
public class TestServiceImpl implements TestService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void test() {
        Trade trade = new Trade();
        trade.setId(10);
        trade.setCommodityId("333");
        trade.setDealName("xxxx");
        trade.save();

        Trade trade1 = new Trade();
        trade1.setId(4);
        trade1.setCommodityId("3332xx33");
        trade1.setDealName("xx3232xx");
        trade1.save();
    }
}
