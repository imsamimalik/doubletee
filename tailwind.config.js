module.exports = {
    content: ["./src/main/resources/templates/**/*.{html,js}"],
    // important: true,
    theme: {
        extend: {
            colors: {
                primary: {
                    100: "#e5f5ff",
                    200: "#b3e1ff",
                    300: "#80cdff",
                    400: "#4db9ff",
                    500: "#2aacff",
                    600: "#228acc",
                    700: "#1d78b3",
                    800: "#004e80",
                    900: "#002f4d",
                },
                secondary: "#ecc94b",
            },
        },
    },
    plugins: [],
};
