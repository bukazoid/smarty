import "bootstrap/dist/css/bootstrap.css";
import ViewsAdmin from "./ViewsAdmin";
import { useSelector } from "react-redux";

export default function Views() {
  const store = useSelector((state) => state);

  return ViewsAdmin();
}
