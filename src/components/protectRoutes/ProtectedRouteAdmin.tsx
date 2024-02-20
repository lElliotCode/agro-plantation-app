import { useEffect, useRef } from "react";
import { checkOpenSession } from "../../interfaces/checkOpenSession";
import { UserRoleType } from "../../context/UserRoleContext";
import { Navigate, Outlet } from "react-router-dom";

type UserDataType = {
	userName: string,
	userRole: UserRoleType
}

function ProtectedRouteAdmin() {
	const axiosController = useRef<AbortController>();

	async function checkAuthorization() {
		axiosController.current = new AbortController();

		try {
			const userData: UserDataType = await checkOpenSession(axiosController.current);

			if (userData.userRole === "admin") {
				return <Outlet />;
			} else {
				return (
					<Navigate to="/" replace />
				);
			}

		} catch {
			return (
				<Navigate to="/login" replace />
			);
		}
	}

	useEffect(() => {

		return () => {
			axiosController.current?.abort();
		};
	});

	return (
		<>
			{checkAuthorization()}
		</>
	);
}

export default ProtectedRouteAdmin;