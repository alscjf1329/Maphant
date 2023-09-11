$(document).ready(function() {
    const graphTitleElement = $(".graph-title h4");
    const dateTextElement = $(".date_txt");
    const chartCanvas = document.getElementById('myChart');
    const ctx = chartCanvas.getContext('2d');
    let chart = null;

    let currentDate = new Date(dateTextElement.text());
    let selectedMonth = currentDate.getMonth() + 1;

    let labels = Array.from({ length: 7 }, (_, i) => {
        const loopDate = new Date(currentDate);
        loopDate.setDate(currentDate.getDate() - i);
        return (
            loopDate.getFullYear() +
            "-" +
            String(loopDate.getMonth() + 1).padStart(2, "0") +
            "-" +
            String(loopDate.getDate()).padStart(2, "0")
        );
    }).reverse();

    function updateGraphTitle(selectedMonth) {
        graphTitleElement.text(selectedMonth + "월의 데이터 통계");
    }

    function requestStatistics(currentDate, labels) {
        $.get({
            url: '/admin/statistics',
            data: {
                currentDate: currentDate.toISOString().substring(0, 10),
                labels: labels
            },
            traditional: true,
            success: function(response) {
                const visitorCountsValueInput = $(response).find('input[name=visitorCounts]');
                const visitorCountsValue = JSON.parse(visitorCountsValueInput.attr('value'));
                updateChart(labels, visitorCountsValue);
                console.log("success");
            },
            error: function(error) {
                console.error('요청 중 에러 발생:', error);
            }
        });
    }


    function updateChart(labels, visitorCountsValue) {
        if (chart) {
            chart.destroy();
        }
        chart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: '방문자수',
                    data: visitorCountsValue,
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 2,
                    fill: false
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: false,
                    },
                },
                interaction: {
                    intersect: false,
                },
                scales: {
                    x: {
                        display: true,
                        title: {
                            display: true
                        }
                    },
                    y: {
                        display: true,
                        title: {
                            display: true,
                            text: 'Value'
                        },
                        suggestedMin: 0,
                        suggestedMax: 100
                    }
                }
            },
        });
    }
    function changeDate(changeAmount) {
        const newDate = new Date(currentDate);
        newDate.setDate(newDate.getDate() + changeAmount);

        if (newDate <= new Date() || newDate.getMonth()) {
            currentDate = newDate;
            selectedMonth = currentDate.getMonth() + 1;

            labels = Array.from({ length: 7 }, (_, i) => {
                const loopDate = new Date(currentDate);
                loopDate.setDate(currentDate.getDate() - i);
                return (
                    loopDate.getFullYear() +
                    "-" +
                    String(loopDate.getMonth() + 1).padStart(2, "0") +
                    "-" +
                    String(loopDate.getDate()).padStart(2, "0")
                );
            }).reverse();

            const year = currentDate.getFullYear();
            const month = String(currentDate.getMonth() + 1).padStart(2, "0");
            const day = String(currentDate.getDate()).padStart(2, "0");
            const dayOfWeek = ["일", "월", "화", "수", "목", "금", "토"][currentDate.getDay()];

            dateTextElement.text(year + "." + month + "." + day + " (" + dayOfWeek + ")");
            updateGraphTitle(selectedMonth);
            requestStatistics(currentDate, labels);
        }
    }
    $(".date_button_on").click(function(e) {
        e.preventDefault();
        changeDate(-1);
    });

    $(".date_button_next").click(function(e) {
        e.preventDefault();
        const today = new Date();
        if(currentDate.getMonth() === today.getMonth()){
            if (currentDate.getDate() < today.getDate()) {
                changeDate(1);
            }
        }
        else {
            changeDate(1);
        }
    });
    updateGraphTitle(selectedMonth);
    requestStatistics(currentDate, labels);
});
