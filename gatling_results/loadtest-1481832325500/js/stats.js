var stats = {
    type: "GROUP",
name: "Global Information",
path: "",
pathFormatted: "group_missing-name-b06d1",
stats: {
    "name": "Global Information",
    "numberOfRequests": {
        "total": "10000",
        "ok": "10000",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "16",
        "ok": "16",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "20131",
        "ok": "20131",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "11595",
        "ok": "11595",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "4373",
        "ok": "4373",
        "ko": "-"
    },
    "percentiles1": {
        "total": "12996",
        "ok": "12996",
        "ko": "-"
    },
    "percentiles2": {
        "total": "14998",
        "ok": "14998",
        "ko": "-"
    },
    "percentiles3": {
        "total": "16374",
        "ok": "16374",
        "ko": "-"
    },
    "percentiles4": {
        "total": "18783",
        "ok": "18783",
        "ko": "-"
    },
    "group1": {
        "name": "t < 800 ms",
        "count": 123,
        "percentage": 1
    },
    "group2": {
        "name": "800 ms < t < 1200 ms",
        "count": 30,
        "percentage": 0
    },
    "group3": {
        "name": "t > 1200 ms",
        "count": 9847,
        "percentage": 98
    },
    "group4": {
        "name": "failed",
        "count": 0,
        "percentage": 0
    },
    "meanNumberOfRequestsPerSecond": {
        "total": "357.143",
        "ok": "357.143",
        "ko": "-"
    }
},
contents: {
"req_is-connected-be193": {
        type: "REQUEST",
        name: "is connected",
path: "is connected",
pathFormatted: "req_is-connected-be193",
stats: {
    "name": "is connected",
    "numberOfRequests": {
        "total": "10000",
        "ok": "10000",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "16",
        "ok": "16",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "20131",
        "ok": "20131",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "11595",
        "ok": "11595",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "4373",
        "ok": "4373",
        "ko": "-"
    },
    "percentiles1": {
        "total": "12996",
        "ok": "12996",
        "ko": "-"
    },
    "percentiles2": {
        "total": "14998",
        "ok": "14998",
        "ko": "-"
    },
    "percentiles3": {
        "total": "16374",
        "ok": "16374",
        "ko": "-"
    },
    "percentiles4": {
        "total": "18783",
        "ok": "18783",
        "ko": "-"
    },
    "group1": {
        "name": "t < 800 ms",
        "count": 123,
        "percentage": 1
    },
    "group2": {
        "name": "800 ms < t < 1200 ms",
        "count": 30,
        "percentage": 0
    },
    "group3": {
        "name": "t > 1200 ms",
        "count": 9847,
        "percentage": 98
    },
    "group4": {
        "name": "failed",
        "count": 0,
        "percentage": 0
    },
    "meanNumberOfRequestsPerSecond": {
        "total": "357.143",
        "ok": "357.143",
        "ko": "-"
    }
}
    }
}

}

function fillStats(stat){
    $("#numberOfRequests").append(stat.numberOfRequests.total);
    $("#numberOfRequestsOK").append(stat.numberOfRequests.ok);
    $("#numberOfRequestsKO").append(stat.numberOfRequests.ko);

    $("#minResponseTime").append(stat.minResponseTime.total);
    $("#minResponseTimeOK").append(stat.minResponseTime.ok);
    $("#minResponseTimeKO").append(stat.minResponseTime.ko);

    $("#maxResponseTime").append(stat.maxResponseTime.total);
    $("#maxResponseTimeOK").append(stat.maxResponseTime.ok);
    $("#maxResponseTimeKO").append(stat.maxResponseTime.ko);

    $("#meanResponseTime").append(stat.meanResponseTime.total);
    $("#meanResponseTimeOK").append(stat.meanResponseTime.ok);
    $("#meanResponseTimeKO").append(stat.meanResponseTime.ko);

    $("#standardDeviation").append(stat.standardDeviation.total);
    $("#standardDeviationOK").append(stat.standardDeviation.ok);
    $("#standardDeviationKO").append(stat.standardDeviation.ko);

    $("#percentiles1").append(stat.percentiles1.total);
    $("#percentiles1OK").append(stat.percentiles1.ok);
    $("#percentiles1KO").append(stat.percentiles1.ko);

    $("#percentiles2").append(stat.percentiles2.total);
    $("#percentiles2OK").append(stat.percentiles2.ok);
    $("#percentiles2KO").append(stat.percentiles2.ko);

    $("#percentiles3").append(stat.percentiles3.total);
    $("#percentiles3OK").append(stat.percentiles3.ok);
    $("#percentiles3KO").append(stat.percentiles3.ko);

    $("#percentiles4").append(stat.percentiles4.total);
    $("#percentiles4OK").append(stat.percentiles4.ok);
    $("#percentiles4KO").append(stat.percentiles4.ko);

    $("#meanNumberOfRequestsPerSecond").append(stat.meanNumberOfRequestsPerSecond.total);
    $("#meanNumberOfRequestsPerSecondOK").append(stat.meanNumberOfRequestsPerSecond.ok);
    $("#meanNumberOfRequestsPerSecondKO").append(stat.meanNumberOfRequestsPerSecond.ko);
}
